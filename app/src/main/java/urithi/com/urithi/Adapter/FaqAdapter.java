package urithi.com.urithi.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import urithi.com.urithi.Model.FaqModel;
import urithi.com.urithi.R;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    ArrayList<FaqModel> faqModelArrayList = new ArrayList<>();
    FaqModel faqModel;
    Context context;

    public FaqAdapter(ArrayList<FaqModel> faqModelArrayList,Context context){
        this.faqModelArrayList = faqModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_raw, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        faqModel = this.faqModelArrayList.get(i);

        viewHolder.faq_n.setText("Q. "+(i+1));
        viewHolder.faq_qus.setText(faqModel.getFaq_ques());
        viewHolder.faq_ans.setText(faqModel.getFaq_ans());

    }

    @Override
    public int getItemCount() {
        return faqModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView faq_n,faq_qus,faq_ans,faq_an;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faq_n = (TextView)itemView.findViewById(R.id.faq_number);
            faq_qus = (TextView)itemView.findViewById(R.id.faq_ques);
            faq_ans = (TextView)itemView.findViewById(R.id.faq_ans_tv);
            faq_an = (TextView)itemView.findViewById(R.id.faq_ans);
            faq_ans.setVisibility(View.GONE);
            faq_an.setVisibility(View.GONE);
            faq_qus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.faq_ques){
                if (faq_ans.getVisibility()==View.VISIBLE && faq_an.getVisibility()==View.VISIBLE){
                    faq_ans.setVisibility(View.GONE);
                    faq_an.setVisibility(View.GONE);
                }else {
                    faq_ans.setVisibility(View.VISIBLE);
                    faq_an.setVisibility(View.VISIBLE);
                    int postion = getAdapterPosition();
                    faqModel = faqModelArrayList.get(postion);
                    faq_ans.setText(faqModel.getFaq_ans());
                }
            }
        }
    }
}
