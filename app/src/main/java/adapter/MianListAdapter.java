package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hs.planktimer.R;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by Peggy on 2014/7/10.
 * potm@163.com
 */
public class MianListAdapter extends BaseAdapter {
	LinkedList<String> datalist = new LinkedList<String>();
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private holder mholder;

	MianListAdapter(Context context){
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public String getItem(int i) {
		return datalist.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if(view == null ){
			view = mLayoutInflater.inflate(R.layout.item_main_list, null);
			mholder.mTv = (TextView)view.findViewById(R.id.item_mainlist_tv);
			view.setTag(mholder);
		}else {
			mholder = (holder)view.getTag();
		}

		return view;
	}

	class holder{
		TextView mTv;
	}
}
