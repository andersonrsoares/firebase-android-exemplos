package br.com.anderson.firebasechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.anderson.firebasechat.R;
import br.com.anderson.firebasechat.model.User;

public class AdapterUser extends BaseAdapter {

    private List<User> objects = new ArrayList<User>();

    private Context context;
    private LayoutInflater layoutInflater;

    public AdapterUser(Context context,List<User> objects ) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public User getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_user, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((User)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(User object, ViewHolder holder) {
        holder.textemail.setText(object.getEmail());
    }

    protected class ViewHolder {
        private TextView textemail;

        public ViewHolder(View view) {
            textemail = (TextView) view.findViewById(R.id.textemail);
        }
    }
}
