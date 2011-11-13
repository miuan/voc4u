package com.voc4u.activity.dictionary;

import junit.framework.Assert;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.voc4u.R;
import com.voc4u.controller.WordController;
import com.voc4u.setting.LangSetting;

public class ItemView extends LinearLayout implements OnCheckedChangeListener
{

	private final WordController mWordCtrl;
	private String mTitle;
	private int mLesson;
	private final CheckBox chkBox;
	private ItemStatus mStatus;
	
	
	public ItemView(Context context, WordController wordCtrl)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.word_setting_item, this);

		mWordCtrl = wordCtrl;
		
		chkBox = (CheckBox)findViewById(R.id.checkbox);
	
		mStatus = ItemStatus.NONE;
	}

	public void setup(int position)
	{
		mLesson = position + 1;
		String lessons[] = getContext().getResources().getStringArray(R.array.lessons);
		
		Assert.assertTrue("Isn't enought names for lessons", position < lessons.length);
		
		if(position < lessons.length)
			mTitle = lessons[position];
		else
			mTitle = getContext().getString(R.string.dictionaryItem, mLesson);
		
		final TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(mTitle);

		final boolean enable = mWordCtrl.isEnableLesson(mLesson);
		chkBox.setChecked(enable);
		chkBox.setOnCheckedChangeListener(this);
		
		Assert.assertTrue("Isn't enought colors for lessons", position < LangSetting.LESSON_BG_COLOR.length);
		if(position < LangSetting.LESSON_BG_COLOR.length)
			setBackgroundResource(LangSetting.LESSON_BG_COLOR[position]);
	}
	
	public int getLesson()
	{
		return mLesson;
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
			boolean isChecked)
	{
		if (!isChecked)
		{
			if (mWordCtrl.isEnableLesson(mLesson))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setMessage("Are you sure you want to disable?")
						.setCancelable(false).setTitle(mTitle)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int id)
									{
										setVocabulary(false);
									}
								}).setNegativeButton("No",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int id)
									{
										dialog.cancel();
										buttonView.setChecked(true);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		} 
		else if(!mWordCtrl.isEnableLesson(mLesson))
			setVocabulary(true);
	}

	protected void setVocabulary(boolean enable)
	{
		if(mStatus == ItemStatus.NONE)
			mStatus = enable ? ItemStatus.ADD : ItemStatus.REMOVE;
		else if(mStatus == ItemStatus.ADD)
			mStatus = !enable ? ItemStatus.NONE : ItemStatus.REMOVE;
		else if(mStatus == ItemStatus.REMOVE)
			mStatus = enable ? ItemStatus.NONE : ItemStatus.REMOVE;
		
		//mWordCtrl.enableLesson(mLesson, enable, null);
	}
	
	public ItemStatus getStatus()
	{
		return mStatus;
	}

	public boolean isChecked() 
	{
		return chkBox.isChecked();
	}

}
