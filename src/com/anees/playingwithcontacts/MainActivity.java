package com.anees.playingwithcontacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ListView listView;
	private List<ContactBean> list = new ArrayList<ContactBean>();

	private Button btnGetSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		btnGetSelected = (Button) findViewById(R.id.btnget);
		btnGetSelected.setOnClickListener(this);

		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		while (phones.moveToNext()) {

			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			ContactBean objContact = new ContactBean();
			objContact.setName(name);
			objContact.setPhoneNo(phoneNumber);
			list.add(objContact);

		}
		phones.close();

		ContactAdapter objAdapter = new ContactAdapter(MainActivity.this, R.layout.alluser_row, list);
		listView.setAdapter(objAdapter);

		if (null != list && list.size() != 0) {
			Collections.sort(list, new Comparator<ContactBean>() {

				@Override
				public int compare(ContactBean lhs, ContactBean rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
			alert.setTitle("");

			alert.setMessage(list.size() + " Contact Found!!!");

			alert.setButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alert.show();

		} else {
			showToast("No Contact Found!!!");
		}
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View v, int position, long id) {

		CheckBox chk = (CheckBox) v.findViewById(R.id.checkbox);
		ContactBean bean = list.get(position);
		if (bean.isSelected()) {
			bean.setSelected(false);
			chk.setChecked(false);
		} else {
			bean.setSelected(true);
			chk.setChecked(true);
		}

		/**
		 * ContactBean bean = (ContactBean)
		 * listview.getItemAtPosition(position);
		 */
		// showCallDialog(bean.getName(), bean.getPhoneNo());
	}

	private void showCallDialog(String name, final String phoneNo) {
		AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
		alert.setTitle("Call?");

		alert.setMessage("Are you sure want to call " + name + " ?");

		alert.setButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.setButton2("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String phoneNumber = "tel:" + phoneNo;
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
				startActivity(intent);
			}
		});
		alert.show();
	}

	@Override
	public void onClick(View v) {
		StringBuffer sb = new StringBuffer();

		// Retrive Data from list
		for (ContactBean bean : list) {

			if (bean.isSelected()) {
				sb.append(bean.getName());
				sb.append(",");
			}
		}

		showAlertView(sb.toString().trim());

	}

	private void showAlertView(String str) {
		AlertDialog alert = new AlertDialog.Builder(this).create();
		if (TextUtils.isEmpty(str)) {
			alert.setTitle("Not Selected");
			alert.setMessage("No One is Seleceted!!!");
		} else {
			// Remove , end of the name
			String strContactList = str.substring(0, str.length() - 1);

			alert.setTitle("Selected");
			alert.setMessage(strContactList);
		}
		alert.setButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
}
