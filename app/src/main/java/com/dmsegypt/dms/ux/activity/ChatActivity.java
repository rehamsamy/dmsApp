package com.dmsegypt.dms.ux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Chat;
import com.dmsegypt.dms.rest.model.MemberChat;
import com.dmsegypt.dms.rest.model.MessageChat;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.ux.adapter.ChatAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amr on 19/12/2017.
 */

public class ChatActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ChatAdapter adapter;
    ArrayList<Chat>chatList;
    DatabaseReference chatReference;
    HashMap<String,String>userInfoMap;
    @BindView(R.id.progress_bar)
    ProgressBar loadingView;
    User current_user;
    @BindArray(R.array.departement)
    String[]deparment;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();
        getPublicChatRooms();

    }


void getPublicChatRooms(){
    final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(Constants.NODE_CHATS).child("10000");
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()){
                getChatList();
            }else{
                HashMap<String,Chat>chatMap=new HashMap<String, Chat>();
                Chat chat1=new Chat();
                chat1.setId("10000");
                chat1.setIsGroup(1);
                chat1.setPrivate(false);
                chat1.setTitle("Company Group");
                chatMap.put(chat1.getId(),chat1);
                for (int i=0;i<deparment.length;i++) {
                    Chat chat2 = new Chat();
                    chat2.setId("10000" + "_" + i);
                    chat2.setIsGroup(1);
                    chat2.setPrivate(false);
                    chat2.setTitle(deparment[i]);
                    chatMap.put(chat2.getId(), chat2);
                }


                FirebaseDatabase.getInstance().getReference().child(Constants.NODE_CHATS).setValue(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getChatList();
                    }
                });

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}



 @OnClick(R.id.fab1)
 void chooseSingleMember(){
    Intent intent=new Intent(ChatActivity.this,MemberListActivity.class);
    startActivity(intent);

 }
    @OnClick(R.id.fab2)
    void createGroup(){
        MaterialDialog.Builder dialog=new MaterialDialog.Builder(ChatActivity.this);
        dialog.title(R.string.hint_group_name).input(R.string.hint_group_name, 0, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog materialDialog, CharSequence charSequence) {
                if (charSequence.toString().isEmpty())
                    return;
                Intent intent=new Intent(ChatActivity.this,MemberListActivity.class);
                intent.putExtra(MemberListActivity.EXTRA_GROUP,true);
                intent.putExtra(MemberListActivity.EXTRA_GROUP_TITLE,charSequence.toString());
                startActivity(intent);
            }
        }).show();
    }
    private void iniView() {
        current_user=App.getInstance().getPrefManager().getUser();
     userInfoMap=new HashMap<>();
        chatReference= FirebaseDatabase.getInstance().getReference();
        chatList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new ChatAdapter(R.layout.item_chat_list,chatList,current_user.getCardId());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent=new Intent(ChatActivity.this,MessageActivity.class);
                intent.putExtra(MessageActivity.EXTRA_CHAT_INFO,chatList.get(i));
                startActivity(intent);
            }
        });
        loadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

    }
   /* void getLastMessage(final Chat chat){
        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_MESSAGES).child(chat.getId()).child(chat.getLast_message_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(MessageChat.class)==null)
                    chat.setLast_message(new MessageChat());
                else
                    chat.setLast_message(dataSnapshot.getValue(MessageChat.class));

                chatList.add(chat);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
    void getChatList(){
        chatReference.child(Constants.NODE_CHATS).orderByChild("private").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                       chatList.clear();
                  for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                      boolean isFound=false;
                      final Chat chat=snapshot.getValue(Chat.class);
                      if (!chat.isPrivate()){
                          if (chat.getMembers()!=null&&!chat.getMembers().containsKey(current_user.getCardId())){
                              MemberChat member=new MemberChat();
                              member.setName(current_user.getFirstName());
                              member.setPush_token(App.getInstance().getPrefManager().getRegId());
                              member.setId(current_user.getCardId());
                              chat.getMembers().put(current_user.getCardId(),member);
                              chatReference.child(Constants.NODE_CHATS).child(chat.getId()).setValue(chat);
                          }else if (chat.getMembers()==null){
                              HashMap<String,MemberChat>memberMap=new HashMap<>();
                              MemberChat member=new MemberChat();
                              member.setName(current_user.getFirstName());
                              member.setPush_token(App.getInstance().getPrefManager().getRegId());
                              member.setId(current_user.getCardId());
                              memberMap.put(current_user.getCardId(), member);
                              chat.setMembers(memberMap);
                              chatReference.child(Constants.NODE_CHATS).child(chat.getId()).setValue(chat);
                          }else {
                              chatList.add(chat);
                          }
                      }else {

                          for (DataSnapshot memberSnap : snapshot.child(Constants.NODE_MEMBERS).getChildren()) {
                              if (memberSnap.getKey().equals(current_user.getCardId())) {
                                  isFound = true;
                                  break;
                              }

                          }
                          if (isFound) {
                              chatList.add(chat);
                          }
                      }

                  }
                  adapter.notifyDataSetChanged();
                loadingView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                loadingView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(ChatActivity.this,R.string.err_data_load_failed, Toast.LENGTH_SHORT).show();
            }
        });

    }





    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.chat_title;
    }
}
