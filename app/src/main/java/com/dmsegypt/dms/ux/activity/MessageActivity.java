package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Chat;
import com.dmsegypt.dms.rest.model.MemberChat;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.MessageChat;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.adapter.MessageChatAdapter;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


/**
 * Created by amr on 20/12/2017.
 */

public class MessageActivity extends BaseActivity implements MessageChatAdapter.OnUploadImageListener {
    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;
     static final String EXTRA_CHAT_INFO ="extra_chatInfo" ;
    @BindView(R.id.recycler_view)
    RecyclerView recycleView;
    @BindView(R.id.message_edit)
    EditText messageEdit;
    @BindView(R.id.btnSend)
    ImageButton sendBtn;
    MessageChatAdapter adapter;
    ArrayList<MessageChat>messages;
    Chat chatInfo;
    DatabaseReference databasereference;
    int PAGE_SIZE=21;
    Uri imageUri;
    private Uri mCapturedImageURI;
    User current_user;
    @BindView(R.id.toolbar_title)
    TextView titletv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        iniView();

    }
    void iniView(){

        current_user=App.getInstance().getPrefManager().getUser();
        databasereference= FirebaseDatabase.getInstance().getReference();
        chatInfo=getIntent().getParcelableExtra(EXTRA_CHAT_INFO);
        if (chatInfo.getIsGroup()==1){
            titletv.setText( chatInfo.getTitle());

        }else {
            for (String key:chatInfo.getMembers().keySet()){
                if (!key.equals(current_user)){
                    titletv.setText( chatInfo.getMembers().get(key).getName());
                }
            }}
        messages=new ArrayList<>();
        adapter=new MessageChatAdapter(this,messages,chatInfo,
                current_user.getCardId());
        recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(adapter);
        adapter.setListener(this);
        getMessages();

        recycleView.addOnScrollListener(scrollListener);

    }

    @OnTouch(R.id.message_edit)
    public boolean onchooseImage(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (messageEdit.getRight() -messageEdit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                   selectImage(this);

                return true;
            }
        }
        return false;
    }





    @OnClick(R.id.btnSend)
    void sendMessage(){
        if (messageEdit.getText().toString().trim().isEmpty()){
            return;
        }
            addMessage();

    }

    HashMap<String, MemberChat> incrementMessageCount(HashMap<String,MemberChat>map){
        for (String key:map.keySet()){
            MemberChat member=map.get(key);
            if (!member.getId().equals(current_user.getCardId())){
            member.setMessage_count(member.getMessage_count()+1);
            map.put(key,member);
            }
        }

        return map;
    }


void createGroup(){
    MessageChat message=new MessageChat();
    message.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(new Date()));
    message.setSent_by(current_user.getCardId());
    message.setMessage("Group Created");
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    String key=ref.child(Constants.NODE_MESSAGES).child(chatInfo.getId()).push().getKey();
    message.setId(key);
    MemberChat member=new MemberChat();
    member.setPush_token(App.getInstance().getPrefManager().getRegId());
    member.setName(current_user.getFirstName());
    member.setId(current_user.getCardId());
    chatInfo.getMembers().put(current_user.getCardId(),member);
    chatInfo.setMembers(incrementMessageCount(chatInfo.getMembers()));
    chatInfo.setLast_message(message);
    HashMap updatedUserData = new HashMap();
    updatedUserData.put(Constants.NODE_CHATS+"/"+chatInfo.getId(),chatInfo);
    updatedUserData.put(Constants.NODE_MESSAGES+"/"+chatInfo.getId()+"/"+key, message);
    ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if (databaseError!=null){
                Toast.makeText(MessageActivity.this, "err", Toast.LENGTH_SHORT).show();
            }
        }
    });
}


    void addImage(String url){
        MessageChat message=new MessageChat();
        message.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(new Date()));
        message.setSent_by(current_user.getCardId());
        message.setImg_url(url);

        if (chatInfo.getMembers()!=null){
            MemberChat member=new MemberChat();
            member.setPush_token(App.getInstance().getPrefManager().getRegId());
            member.setName(current_user.getFirstName());
            member.setId(current_user.getCardId());
            chatInfo.getMembers().put(current_user.getCardId(),member);
        }else {
            HashMap<String,MemberChat>memberMap=new HashMap<>();
            MemberChat member=new MemberChat();
            member.setName(current_user.getFirstName());
            member.setPush_token(App.getInstance().getPrefManager().getRegId());
            member.setId(current_user.getCardId());
            memberMap.put(current_user.getCardId(), member);
            chatInfo.setMembers(memberMap);
        }

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        String key=ref.child(Constants.NODE_MESSAGES).child(chatInfo.getId()).push().getKey();
        message.setId(key);
        chatInfo.setMembers(incrementMessageCount(chatInfo.getMembers()));
        chatInfo.setLast_message(message);
        HashMap updatedUserData = new HashMap();
        updatedUserData.put(Constants.NODE_CHATS+"/"+chatInfo.getId(),chatInfo);
        updatedUserData.put(Constants.NODE_MESSAGES+"/"+chatInfo.getId()+"/"+key, message);
        ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Toast.makeText(MessageActivity.this, "err", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void addMessage(){
        MessageChat message=new MessageChat();
        message.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(new Date()));
        message.setSent_by(current_user.getCardId());
        message.setMessage(messageEdit.getText().toString().trim());

        if (chatInfo.getMembers()!=null){
            MemberChat member=new MemberChat();
            member.setName(current_user.getFirstName());
            member.setPush_token(App.getInstance().getPrefManager().getRegId());
            member.setId(current_user.getCardId());
        chatInfo.getMembers().put(current_user.getCardId(),member);
        }else {
            HashMap<String,MemberChat>memberMap=new HashMap<>();
            MemberChat member=new MemberChat();
            member.setName(current_user.getFirstName());
            member.setPush_token(App.getInstance().getPrefManager().getRegId());
            member.setId(current_user.getCardId());
            memberMap.put(current_user.getCardId(), member);
            chatInfo.setMembers(memberMap);
        }

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        String key=ref.child(Constants.NODE_MESSAGES).child(chatInfo.getId()).push().getKey();
        message.setId(key);
        chatInfo.setMembers(incrementMessageCount(chatInfo.getMembers()));
        chatInfo.setLast_message(message);
        HashMap updatedUserData = new HashMap();
        updatedUserData.put(Constants.NODE_CHATS+"/"+chatInfo.getId(),chatInfo);
        updatedUserData.put(Constants.NODE_MESSAGES+"/"+chatInfo.getId()+"/"+key, message);
        ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Toast.makeText(MessageActivity.this, "err", Toast.LENGTH_SHORT).show();
                }
            }
        });
        messageEdit.setText("");
    }


    void getMessages(String index){
        databasereference.child(Constants.NODE_MESSAGES).child(chatInfo.getId()).orderByKey().endAt(index).limitToLast(PAGE_SIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MessageChat>newMessages=new ArrayList<MessageChat>();
                HashMap updates=new HashMap();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageChat message = snapshot.getValue(MessageChat.class);
                    if (!message.getSent_by().equals(current_user.getCardId())&&message.getIs_seen()==0){
                        message.setIs_seen(1);
                        updates.put(Constants.NODE_MESSAGES+"/"+chatInfo.getId()+"/"+message.getId(),message);
                    }
                    newMessages.add(message);
                }

                if (updates.size()>0){
                    if(chatInfo.getMembers().containsKey(current_user.getCardId())){
                        chatInfo.getMembers().get(current_user.getCardId()).setMessage_count(0);
                        if(!chatInfo.getLast_message().getSent_by().equals(current_user.getCardId())){
                            chatInfo.getLast_message().setIs_seen(1);
                        }
                    }
                    updates.put(Constants.NODE_CHATS+"/"+chatInfo.getId(),chatInfo);
                    databasereference.updateChildren(updates);
                }
                if (!newMessages.isEmpty()){
                Collections.reverse(newMessages);
                newMessages.remove(0);
                messages.addAll(newMessages);
                adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ValueEventListener messageListneer=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            messages.clear();
            final HashMap updates = new HashMap();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                MessageChat message = snapshot.getValue(MessageChat.class);
                if (!message.getSent_by().equals(current_user.getCardId()) && message.getIs_seen() == 0) {
                    message.setIs_seen(1);
                    updates.put(Constants.NODE_MESSAGES + "/" + chatInfo.getId() + "/" + message.getId(), message);
                }
                messages.add(message);
            }

            if (updates.size() > 0) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child(Constants.NODE_CHATS).child(chatInfo.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            chatInfo = dataSnapshot.getValue(Chat.class);

                            if (chatInfo.getMembers().containsKey(current_user.getCardId())) {
                                chatInfo.getMembers().get(current_user.getCardId()).setMessage_count(0);
                                if (!chatInfo.getLast_message().getSent_by().equals(current_user.getCardId())) {
                                    chatInfo.getLast_message().setIs_seen(1);
                                }
                            }
                            updates.put(Constants.NODE_CHATS + "/" + chatInfo.getId(), chatInfo);
                            databasereference.updateChildren(updates);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if (chatInfo.getIsGroup() == 1 && chatInfo.isPrivate() && messages.isEmpty()) {
                createGroup();
            }

            Collections.reverse(messages);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



        void getMessages() {
        databasereference.child(Constants.NODE_MESSAGES).child(chatInfo.getId()).orderByKey().limitToLast(PAGE_SIZE).addValueEventListener(messageListneer);
    }
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_message;
    }

    @Override
    public int getToolbarTitle() {
       return R.string.chat_title;
    }
    private RecyclerView.OnScrollListener scrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isLastItemDisplaying(recyclerView)) {
                getMessages(messages.get(messages.size()-1).getId());
           }
        }
    };
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }


    public void selectImage(final Activity context) {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.add_new_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        mCapturedImageURI= ImagesHandler.getInstance().cameraIntent(context);
                    else
                        ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isCorrectImage = false;
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IntentManager.KEY_PICK_IMAGE) {
                imageUri =data.getData();
                if (imageUri != null) {
                  MessageChat message=new MessageChat();
                    message.setUri(imageUri);
                    message.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(new Date()));
                    message.setSent_by(current_user.getCardId());
                    message.setMessage("");
                    messages.add(0,message);
                    adapter.notifyDataSetChanged();

                    /*
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    imgView.setImageBitmap(previewImage);
                    btnaddrequest.setVisibility(View.VISIBLE);*/





                }



            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {/*
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    imgView.setImageBitmap(previewImage);
                    btnaddrequest.setVisibility(View.VISIBLE);*/







                }

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().cameraIntent(MessageActivity.this);
                else
                    //      Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(MessageActivity.this, "Permission Denied", 3000).show();

                break;
            case REQUESTCODE_STORAGE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().galleryIntent(this);
                else
                    //    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(MessageActivity.this, getString(R.string.permission_denied), 3000).show();

                break;
        }
    }


    @Override
    public void onImageUpload(String url) {
        addImage(url);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databasereference!=null){
            databasereference=FirebaseDatabase.getInstance().getReference().child("0");
        }
    }
}
