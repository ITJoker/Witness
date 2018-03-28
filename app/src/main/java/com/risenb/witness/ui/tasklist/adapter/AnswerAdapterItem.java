package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.beans.CheckBoxState;
import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.ui.tasklist.EventMessage;
import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.views.newViews.AnFQNumEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswerAdapterItem extends BaseAdapter {
    private Context context;
    private List<String> answerlist;
    private String type;
    int index = -1;

    private boolean isInit = false;
    private boolean isInitCheBox = false;

    private int isRight = 0;


    private int indexCheckBox = -1;
    private CheckBoxState checkBoxState;
    private static HashMap<Integer, Boolean> isSelected;
    List<Map<Integer, Boolean>> mapList = new ArrayList<>();
    private String id;
    private List<ExecTaskInfo.AnswerBean> Answerlist;

    private List<Integer> integers = new ArrayList<>();

    private int ps;
    private String textContent;

    public AnswerAdapterItem(Context context, List<String> answerlist, String type, String testId) {
        this.context = context;
        this.answerlist = answerlist;
        this.type = type;
        this.id = testId;
        isSelected = new HashMap<>();
        checkBoxState = new CheckBoxState();
    }

    public void setList(List<ExecTaskInfo.AnswerBean> Answerlist) {
        this.Answerlist = Answerlist;
    }

    @Override
    public int getCount() {
        if (type.equals("3")) {
            return 1;
        }
        if (type.equals("2")) {

            for (int i = 0; i < answerlist.size(); i++) {

                isSelected.put(i, false);
            }
//            checkBoxState.setIsSelected(isSelected);
//            checkBoxState.setId(id);
//            setCheckBoxState(checkBoxState);
        }
        return answerlist.size();
    }

    @Override
    public Object getItem(int position) {
        return answerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void selectCheckBoxPosition(int index) {
        indexCheckBox = index;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View mSingleLayoutView;
        View mCheckBoxLayoutView;
        View mAreaLayoutView;
        Log.e("getView", type + "");
        if (type.equals("1")) {
            final ViewHolder viewHolderone;

            if (view == null) {
                mSingleLayoutView = LayoutInflater.from(context).inflate(R.layout.parentlistview_item, parent, false);
                viewHolderone = new ViewHolder(mSingleLayoutView);
                mSingleLayoutView.setTag(viewHolderone);
                view = mSingleLayoutView;
            } else {

                viewHolderone = (ViewHolder) view.getTag();
            }
            viewHolderone.mAnwerText.setText(answerlist.get(position) + "");

            //核心方法，判断单选按钮被按下的位置与之前的位置是否相等，然后做相应的操作。

            if (!isInit) {
                if (null != Answerlist && Answerlist.size() > 0) {
                    for (int i = 0; i < Answerlist.size(); i++) {
                        if (Answerlist.get(i).getType().equals("1")) {
                            if (Answerlist.get(i).getSolution().get(0).equals(answerlist.get(position))) {
                                Log.e("单选", Answerlist.get(i).getSolution().get(0) + "---" + ps);
                                index = position;
                                //notifyDataSetChanged();
                                isInit = true;
                            }
                        }
                    }
                }

            }


            viewHolderone.mLayoutSelectRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    index = position;
                    notifyDataSetChanged();
                }
            });

            viewHolderone.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        index = position;
                        notifyDataSetChanged();
                    }
                }
            });


            if (index == position) {// 选中的条目和当前的条目是否相等
                viewHolderone.mLayoutSelectRadioButton.setBackgroundResource(R.drawable.task_situation_select);
                viewHolderone.mRadioButton.setChecked(true);
                EventBus.getDefault().post(new EventMessage.Mesage(answerlist.get(position) + "", id));
                //  Log.e("单选",position+"");
            } else {
                viewHolderone.mLayoutSelectRadioButton.setBackgroundResource(R.drawable.task_situation_normal);
                viewHolderone.mRadioButton.setChecked(false);
            }
            Log.e("单选", position + "");
        }

        if (type.equals("2")) {
            final ViewHolderTwo viewHolderTwo;
            if (view == null) {
                mCheckBoxLayoutView = LayoutInflater.from(context).inflate(R.layout.listitem_chebox, parent, false);
                viewHolderTwo = new ViewHolderTwo(mCheckBoxLayoutView);
                mCheckBoxLayoutView.setTag(viewHolderTwo);
                view = mCheckBoxLayoutView;
            } else {
                viewHolderTwo = (ViewHolderTwo) view.getTag();
            }

            // if(!isInitCheBox){
            if (Answerlist.size() > 0) {
                for (int i = 0; i < Answerlist.size(); i++) {
                    if (Answerlist.get(i).getType().equals("2")) {
                        for (int k = 0; k < Answerlist.get(i).getSolution().size(); k++) {
                            if (answerlist.get(position).equals(Answerlist.get(i).getSolution().get(k))) {
                                Log.e("答案2", answerlist.get(position) + "----" + position);
                                viewHolderTwo.mCheckBox.setChecked(true);
                                viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_select);
                                isSelected.put(position, true);
                                checkBoxState.setIsSelected(isSelected);
                                checkBoxState.setId(id);
                                setCheckBoxState(checkBoxState);
                                k = Answerlist.get(i).getSolution().size();

                            } else {
                                viewHolderTwo.mCheckBox.setChecked(false);
                                viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_normal);
                                isSelected.put(position, false);
                                checkBoxState.setIsSelected(isSelected);
                                checkBoxState.setId(id);
                                setCheckBoxState(checkBoxState);
                                Log.e("答案3", answerlist.get(position) + "----" + position);
                            }

                        }
                    }
                }
                //}
                // isInitCheBox=true;
                //  notifyDataSetChanged();
            }

            viewHolderTwo.mCheckBoxText.setText(answerlist.get(position) + "");

            viewHolderTwo.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // getIsSelected().get(position)
                    if (viewHolderTwo.mCheckBox.isChecked()) {
                        Log.e("getIsSelected().get(position)", getIsSelected().get(position) + "");
                        Log.e("position", position + "-----" + viewHolderTwo.mCheckBox.isChecked());
                        viewHolderTwo.mCheckBox.setChecked(false);
                        viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_normal);
                        isSelected.put(position, false);
                        EventBus.getDefault().post(new EventMessage.MesageCheckBox(isSelected, id));
                        checkBoxState.setIsSelected(isSelected);
                        checkBoxState.setId(id);
                        setCheckBoxState(checkBoxState);
                    } else {
                        viewHolderTwo.mCheckBox.setChecked(true);
                        viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_select);
                        isSelected.put(position, true);
                        EventBus.getDefault().post(new EventMessage.MesageCheckBox(isSelected, id));
                        checkBoxState.setIsSelected(isSelected);
                        checkBoxState.setId(id);
                        setCheckBoxState(checkBoxState);
                    }

                }
            });

            viewHolderTwo.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.e("选前状态", isChecked + "");
                    if (isChecked) {
                        Log.e("isChecked", answerlist.get(position) + "-----" + position);
                        viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_select);
                        viewHolderTwo.mCheckBox.setChecked(true);
                        isSelected.put(position, true);
                        setIsSelected(isSelected, id);
                        EventBus.getDefault().post(new EventMessage.MesageCheckBox(isSelected, id));
                        checkBoxState.setIsSelected(isSelected);
                        checkBoxState.setId(id);
                        setCheckBoxState(checkBoxState);
                    } else {
                        Log.e("选后状态", isChecked + "");
                        Log.e("isChecked2", answerlist.get(position) + "");
                        viewHolderTwo.mRootLayout.setBackgroundResource(R.drawable.task_situation_normal);
                        viewHolderTwo.mCheckBox.setChecked(false);
                        isSelected.put(position, false);
                        EventBus.getDefault().post(new EventMessage.MesageCheckBox(isSelected, id));
                        setIsSelected(isSelected, id);
                        checkBoxState.setIsSelected(isSelected);
                        checkBoxState.setId(id);
                        setCheckBoxState(checkBoxState);
                    }
                }
            });
        }

        if (type.equals("3")) {
            ViewHolderThree viewHolderthree;
            if (view == null) {
                mAreaLayoutView = LayoutInflater.from(context).inflate(R.layout.listitem_area, parent, false);
                viewHolderthree = new ViewHolderThree(mAreaLayoutView);
                mAreaLayoutView.setTag(viewHolderthree);
                view = mAreaLayoutView;
            } else {

                viewHolderthree = (ViewHolderThree) view.getTag();
            }
            String content = null;

            if (null != Answerlist && Answerlist.size() > 0) {
                for (int i = 0; i < Answerlist.size(); i++) {
                    if (Answerlist.get(i).getType().equals("3")) {
                        content = Answerlist.get(i).getSolution().get(0);
                    }
                }
            }
            if (null != content) {
                viewHolderthree.mAnFQNumEditText
                        .setEtMinHeight(200)//设置最小高度，单位px
                        .setLength(50)//设置总字数
                        .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
                        .setLineColor("#3F51B5")
                        .setContentId(id)//设置横线颜色
                        .show();
                viewHolderthree.mAnFQNumEditText.setText(content);
            } else {
                viewHolderthree.mAnFQNumEditText.setEtHint("请输入内容")//设置提示文字
                        .setEtMinHeight(200)//设置最小高度，单位px
                        .setLength(50)//设置总字数
                        .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
                        .setLineColor("#3F51B5")
                        .setContentId(id)//设置横线颜色
                        .setContentId(id)
                        .show();
            }

            textContent = viewHolderthree.mAnFQNumEditText.getTextContent();
            if (null == textContent) {
                textContent = "";
            } else {
                Log.e("textContent", textContent.toString());
            }
        }
        return view;
    }

    public static class ViewHolder {
        @BindView(R.id.anwer_text)
        TextView mAnwerText;

        @BindView(R.id.radioBtn)
        RadioButton mRadioButton;

        @BindView(R.id.layout_select_radiobutton)
        LinearLayout mLayoutSelectRadioButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class ViewHolderTwo {
        @BindView(R.id.listview_checkbox_text)
        TextView mCheckBoxText;
        @BindView(R.id.listview_checkbox_button)
        CheckBox mCheckBox;
        @BindView(R.id.layout_checkbox)
        RelativeLayout mRootLayout;

        ViewHolderTwo(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class ViewHolderThree {
        @BindView(R.id.list_item_area_input)
        AnFQNumEditText mAnFQNumEditText;

        ViewHolderThree(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected, String id) {
        AnswerAdapterItem.isSelected = isSelected;

    }

    public CheckBoxState getCheckBoxState() {
        return checkBoxState;
    }

    public void setCheckBoxState(CheckBoxState checkBoxState) {
        this.checkBoxState = checkBoxState;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
