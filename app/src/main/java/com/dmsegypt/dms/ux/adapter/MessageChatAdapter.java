package com.dmsegypt.dms.ux.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Chat;
import com.dmsegypt.dms.rest.model.MessageChat;
import com.dmsegypt.dms.utils.utils;
import com.dmsegypt.dms.ux.custom_view.UploadImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amr on 20/12/2017.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MessgaeVieweHolder> {
    ArrayList<MessageChat>messsages;
    Chat chatinfo;
    String userID;
    public static final int ITEM_VIEW_OWNER=0;
    public static final int ITEM_VIEW_user=1;
    String Storage_Path = "All_Image_Uploads/";
    private Context context;


    public MessageChatAdapter(Context context,ArrayList<MessageChat> messsages, Chat chatinfo, String userID) {
        this.messsages = messsages;
        this.chatinfo = chatinfo;
        this.userID = userID;
        this.context=context;
    }



    @Override
    public MessgaeVieweHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        if(viewType==ITEM_VIEW_OWNER){
            view=inflater.inflate(R.layout.item_chat_right,null,false);

        }else {
            view=inflater.inflate(R.layout.item_chat_left,null,false);
        }
        return new MessgaeVieweHolder(view);
    }

    @Override
    public void onBindViewHolder(MessgaeVieweHolder holder, int position) {
        holder.binView(messsages.get(position));
    }

    @Override
    public int getItemCount() {
        return messsages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messsages.get(position).getSent_by().equals(userID)){
            return ITEM_VIEW_OWNER;

        }
        else {
            return ITEM_VIEW_user;
        }
    }

    class MessgaeVieweHolder extends RecyclerView.ViewHolder implements UploadImageView.OnUploadImageClickListener {
        @BindView(R.id.txt_msg)
        TextView msgTv;
        @BindView(R.id.txt_date)
        TextView dateTv;
        @BindView(R.id.txt_name)
        TextView nameTextView;
        @BindView(R.id.seen_imgv)
        ImageView seenImgview;
        @BindView(R.id.upload_imgv)
        UploadImageView uploadimgv;

        public MessgaeVieweHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            uploadimgv.setUploadImageClickListener(this);
        }
        void binView(MessageChat messageChat){

           msgTv.setText(messageChat.getMessage());
           dateTv.setText(messageChat.getDate());
            if (chatinfo.getIsGroup()==1){
                nameTextView.setVisibility(View.VISIBLE);
                if (messageChat.getSent_by().equals(userID)){
                    nameTextView.setText("You");
                }else{
                    nameTextView.setText(chatinfo.getMembers().get(messageChat.getSent_by()).getName());
                }
            }else {
                nameTextView.setVisibility(View.GONE);
            }
            if (messageChat.getSent_by().equals(userID)){
                seenImgview.setVisibility(View.VISIBLE);
                seenImgview.setImageResource(messageChat.getIs_seen()==1?R.drawable.ic_seen:R.drawable.ic_unseen);
            }else {
                seenImgview.setVisibility(View.GONE);
            }
            if (messageChat.getImg_url()!=null&&messageChat.getUri()==null){
                uploadimgv.setVisibility(View.VISIBLE);
                uploadimgv.showState(UploadImageView.DEFAULT_STATE);
                uploadimgv.PreviewImage(Uri.parse(messageChat.getImg_url()));

            }else if (messageChat.getUri()!=null&&messageChat.getImg_url()==null){
                if (messageChat.getImageState()==UploadImageView.DEFAULT_STATE){
                seenImgview.setVisibility(View.GONE);
                uploadimgv.setVisibility(View.VISIBLE);
                uploadimgv.PreviewImage(messageChat.getUri());
                uploadimgv.showState(UploadImageView.LOADING_STATE);
                messageChat.setImageState(UploadImageView.LOADING_STATE);
                UploadImageFileToFirebaseStorage(uploadimgv,messageChat,getAdapterPosition());
                }else if (messageChat.getImageState()==UploadImageView.LOADING_STATE){
                    seenImgview.setVisibility(View.GONE);
                    uploadimgv.setVisibility(View.VISIBLE);
                    uploadimgv.PreviewImage(messageChat.getUri());
                    uploadimgv.showState(UploadImageView.LOADING_STATE);
                }else if (messageChat.getImageState()==UploadImageView.FAILED_STATE){
                    seenImgview.setVisibility(View.GONE);
                    uploadimgv.setVisibility(View.VISIBLE);
                    uploadimgv.PreviewImage(messageChat.getUri());
                    uploadimgv.showState(UploadImageView.FAILED_STATE);
                }
            }else  {
                uploadimgv.setVisibility(View.GONE);
            }











        }

        @Override
        public void onCloseClicked(View view) {

        }

        @Override
        public void onRetryClicked(View view) {
            uploadimgv.showState(UploadImageView.LOADING_STATE);
            UploadImageFileToFirebaseStorage(uploadimgv,messsages.get(getAdapterPosition()),getAdapterPosition());
        }

        @Override
        public void onAddClicked(View view) {
               }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = context.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public void UploadImageFileToFirebaseStorage(final UploadImageView uploadimgv, final MessageChat message, final int pos) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(Constants.NODE_MESSAGES).child(chatinfo.getId());
        if (message.getUri() != null) {


            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(message.getUri()));

            storageReference2nd.putFile(message.getUri())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadimgv.showState(UploadImageView.DEFAULT_STATE);

                            @SuppressWarnings("VisibleForTests")
                            String img_url=taskSnapshot.getDownloadUrl().toString();
                            if (listener!=null){
                                listener.onImageUpload(img_url);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                              message.setImageState(UploadImageView.FAILED_STATE);
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    });
        } else {

        }
    }
    OnUploadImageListener listener;
   public interface OnUploadImageListener{
        void onImageUpload(String url);
    }

    public void setListener(OnUploadImageListener listener) {
        this.listener = listener;
    }
}