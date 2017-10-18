package com.slyfoxstudios.korwinsoundboard;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import static com.facebook.messenger.MessengerUtils.hasMessengerInstalled;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private Fragment _context_fragment;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private MediaPlayerPlay _mpp;
    private Ads _ads;
    LayoutInflater _inflater;
    RingTone ringTone;
    DatabaseHelper myDb;
    private IRateUsClicksListener iClickListener;

    private Dialog dialog;
    //   public static String PACKAGE_NAME;

    public ExpandableListAdapter(Context context,Fragment context_fragment, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData,
                                 MediaPlayerPlay mpp, Ads ads, LayoutInflater inflater, DatabaseHelper dbhelp)
    {
        this._context_fragment=context_fragment;
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._mpp = mpp;
        this._ads = ads;
        this._inflater = inflater;
        myDb = dbhelp;

        iClickListener = (MainActivity)_context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        final DatabaseHelper myDb = new DatabaseHelper(_context);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
            final ExpandableListView explistview1 = (ExpandableListView) convertView.findViewById(R.id.lst_1);
            final ExpandableListView explistview2 = (ExpandableListView) convertView.findViewById(R.id.lst_2);
            final ExpandableListView explistview3 = (ExpandableListView) convertView.findViewById(R.id.lst_2);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.lblListItem);
            viewHolder.btnShare = (ImageButton) convertView.findViewById(R.id.btn_Share);
            viewHolder.btnNotif = (ImageButton) convertView.findViewById(R.id.btn_Notif);

            viewHolder.btnShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String mimeType = "audio/mpeg";
                    Uri uri = Uri.parse(_context.getResources().getString(R.string.path_to_raw_folder)
                            + myDb.GetSoundDirById(getFirstWord(viewHolder.txtTitle.getText().toString())));

                    if (!hasMessengerInstalled(_context))
                    {
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        myIntent.setType(mimeType);
                        _context.startActivity(Intent.createChooser(myIntent, "Share using"));
                    }
                    // Messenger Sharing
                    else
                    {
                        ShareToMessengerParams shareToMessengerParams =
                                ShareToMessengerParams.newBuilder(uri, mimeType).build();
                        final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
                        MessengerUtils.shareToMessenger(
                                (Activity) _context,
                                REQUEST_CODE_SHARE_TO_MESSENGER,
                                shareToMessengerParams);
                    }
                }
            });

            viewHolder.btnNotif.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CreateRingtonePopup(viewHolder);
                }
            });

            viewHolder.txtTitle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    PlayByID(getFirstWord(viewHolder.txtTitle.getText().toString()), myDb);
                    MainActivity.clicks++;
                    Log.d("clicks: ",MainActivity.clicks+"");
                    iClickListener.CheckIfRated(MainActivity.clicks);

                    SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();

                    mEditor.putInt("clicks", MainActivity.clicks).commit();
                    if (_context.getResources().getBoolean(R.bool.ads_on))
                    {
                        _ads.displayInterstitial();

                    }
                }
            });
        }


        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }
    private String getFirstWord(String text) {
        text = text.substring(0,3);
        if(text.substring(1,2)==".")
            return text.substring(0,1);
        else if(text.substring(2,3)==".")
            return text.substring(0,2);
        else
            return text;
    }
    public void PlayByID(String Id, DatabaseHelper db)
    {
        _mpp.Play(Id,db);

    }
    public class ViewHolder
    {
        TextView txtTitle ;
        ImageButton btnShare;
        ImageButton btnNotif;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        Button lblListHeader = (Button) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        lblListHeader.setFocusable(false);
        lblListHeader.setClickable(false);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void CreateRingtonePopup(final ViewHolder viewHolder)
    {
        ringTone = new RingTone(_context);
        View alertLayout = _inflater.inflate(R.layout.layout_custom_dialog, null);
        final Button btnSound = (Button) alertLayout.findViewById(R.id.btn_sounds);
        final Button btnSms = (Button) alertLayout.findViewById(R.id.btn_sms);
        final Button btnAlarm = (Button) alertLayout.findViewById(R.id.btn_alarm);

        AlertDialog.Builder alert = new AlertDialog.Builder(_context);
        alert.setView(alertLayout);
        alert.setCancelable(false);

        dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        btnSound.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ringTone.ChangeRingTone(RingtoneManager.TYPE_RINGTONE,
                        myDb.GetSoundDirById(getFirstWord(viewHolder.txtTitle.getText().toString())),
                        myDb.GetSoundTitleById(getFirstWord(viewHolder.txtTitle.getText().toString())));
                dialog.dismiss();
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ringTone.ChangeRingTone(RingtoneManager.TYPE_NOTIFICATION,
                        myDb.GetSoundDirById(getFirstWord(viewHolder.txtTitle.getText().toString())),
                        myDb.GetSoundTitleById(getFirstWord(viewHolder.txtTitle.getText().toString())));
                dialog.dismiss();
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ringTone.ChangeRingTone(RingtoneManager.TYPE_ALARM,
                        myDb.GetSoundDirById(getFirstWord(viewHolder.txtTitle.getText().toString())),
                        myDb.GetSoundTitleById(getFirstWord(viewHolder.txtTitle.getText().toString())));
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener()
        {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                }
                return true;
            }
        });
    }

}
