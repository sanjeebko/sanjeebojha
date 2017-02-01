package np.com.sanjeebojha.sanjeeb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by sanjeeb on 09/01/2017.
 */
// tutorial http://www.truiton.com/2015/03/android-cardview-example/

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.DataObjectHolder> {
    private static RecyclerViewClickListener clickListener;
    private List<ContactMessage> contactMessageList;
    private Context con;
    public MainActivityRecyclerViewAdapter(List<ContactMessage> contactMessageList){
        this.contactMessageList=contactMessageList;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String name = parent.getContext().getPackageName();
        con =parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row,parent,false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if(holder.id==null){
            Common.SetToast(con,"Error",true);
        }
        int id =contactMessageList.get(position).getId();
        holder.id.setText("id="+id);
        holder.phone.setText(contactMessageList.get(position).getPhone());
        holder.message.setText(contactMessageList.get(position).getMessage());
        Date blahDate =contactMessageList.get(position).getMessageTime();
        if(blahDate==null)
            blahDate = new Date(2016, 1, 1);
        holder.messageTime.setText(blahDate.toString());

        ((MainActivityRecyclerViewAdapter) this).setOnItemClickListener(new MainActivityRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                TextView tv= (TextView) v.findViewById(R.id.a1_cv_message_text);
                if(tv!=null)
                    Common.SetToast(v.getContext(),tv.getText().toString(),true);
                deleteItem(position);
            }
        });
    }

    public void addItem(ContactMessage message,int index){
        contactMessageList.add(index,message);
        notifyItemInserted(index);
    }
    public void deleteItem(int index){
        contactMessageList.remove(index );
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return contactMessageList.size();
    }

    public void setOnItemClickListener(RecyclerViewClickListener clickListener){
        this.clickListener = clickListener;
    }



    public static class DataObjectHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        TextView id;
        TextView phone;
        TextView message;
        TextView messageTime;
        public DataObjectHolder(View itemView){
            super(itemView);
            id= (TextView) itemView.findViewById(R.id.a1_cv_id_text);
            phone= (TextView) itemView.findViewById(R.id.a1_cv_phone_text);
            message= (TextView) itemView.findViewById(R.id.a1_cv_message_text);
            messageTime= (TextView) itemView.findViewById(R.id.a1_cv_messagetime_text);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(clickListener!=null)
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public interface RecyclerViewClickListener{
        public void onItemClick(int position,View v);
    }
}
