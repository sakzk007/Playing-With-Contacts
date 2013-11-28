package com.anees.playingwithcontacts;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<ContactBean> {

	private Activity activity;
	private List<ContactBean> items;
	private int row;
	private ContactBean objBean;

	public ContactAdapter(Activity act, int row, List<ContactBean> items) {
		super(act, row, items);

		this.activity = act;
		this.row = row;
		this.items = items;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(row, null);

			holder = new ViewHolder();
			holder.tvname = (TextView) convertView.findViewById(R.id.tvname);
			holder.tvPhoneNo = (TextView) convertView.findViewById(R.id.tvphone);
			holder.chk = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton view, boolean isChecked) {
					int getPosition = (Integer) view.getTag();
					items.get(getPosition).setSelected(view.isChecked());

				}
			});
			convertView.setTag(holder);
			convertView.setTag(R.id.tvname, holder.tvname);
			convertView.setTag(R.id.tvphone, holder.tvPhoneNo);
			convertView.setTag(R.id.checkbox, holder.chk);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if ((items == null) || ((position + 1) > items.size())) {
			return convertView;
		}
		holder.chk.setTag(position);
		objBean = items.get(position);

		if (holder.tvname != null && null != objBean.getName() && objBean.getName().trim().length() > 0) {
			holder.tvname.setText(Html.fromHtml(objBean.getName()));
		}
		if (holder.tvPhoneNo != null && null != objBean.getPhoneNo() && objBean.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText(Html.fromHtml(objBean.getPhoneNo()));
		}
		return convertView;
	}

	public static class ViewHolder {
		protected TextView tvname, tvPhoneNo;
		protected CheckBox chk;
	}

}
