package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.beans.MutilPartInfo;
import com.risenb.witness.beans.RadioBean;
import com.risenb.witness.beans.RadioButtonInfo;
import com.risenb.witness.ui.tasklist.CheckBoxInfo;
import com.risenb.witness.ui.tasklist.InterImage;
import com.risenb.witness.ui.tasklist.LastTaskSaveAndUpLoad;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.MyListView;
import com.risenb.witness.views.newViews.AnFQNumEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LastTaskCommconAdapter extends BaseAdapter implements InterImage {

    private Context mContext;

    //图片和视频
    private List<LastTaskBean.TaskListBean> mList;
    private Map<Integer, MutilPartInfo> list = new HashMap<>();//记录图片拍照位置和录像拍照位置
    private LayoutInflater mLayoutInflater;

    //上刊状态 单选
    private List<String> radiostr;
    private String radiostrType;
    private int index = -1;
    private String selectstr = null;
    private RadioBean radioinfo;

    private boolean selected = false;
    private int whereindex = -1;

    //图片和视频
    public LastTaskCommconAdapter(Context context, List<LastTaskBean.TaskListBean> mList, int index) {

        this.mContext = context;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.whereindex = index;
        ((LastTaskSaveAndUpLoad) mContext).setListener(this);


    }

    //上刊状态 单选 选中的只
    public void setselectstr(String str) {
        this.selectstr = str;
        // notifyDataSetChanged();
    }

    //上刊状态 单选 选中位置
    public void setSelectItem(int selectItem) {
        this.index = selectItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mList && !mList.isEmpty()) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (null != mList && !mList.isEmpty()) {
            return mList.get(position);
        }
        return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View rootview = view;
        String type = mList.get(position).getIsType();
        //图片和视频
        if ("1".equals(type) || "0".equals(type)) {
            if (null != rootview) {
                rootview = null;
                rootview = ShowImageAndRecordVideo(position, rootview, parent, type);
            } else {
                rootview = ShowImageAndRecordVideo(position, rootview, parent, type);
            }
        }

        if ("2".equals(type)) {
            View ViewIssueStae = null;
            ViewHolderIssueStae mViewHolderIssueStae = null;
            final int position1 = position;
            if (view == null) {
                ViewIssueStae = mLayoutInflater.inflate(R.layout.issue_state_list, parent, false);
                mViewHolderIssueStae = new ViewHolderIssueStae(ViewIssueStae);
                ViewIssueStae.setTag(mViewHolderIssueStae);
                rootview = ViewIssueStae;
            } else {
                mViewHolderIssueStae = (ViewHolderIssueStae) view.getTag();
            }

            mViewHolderIssueStae.issueSingleProblem.setText(mList.get(position).getTaskRemark());
            List<String> mRadioText = mList.get(position).getExampleFile();
//            RadioChildAdapter radioChildAdapter = new RadioChildAdapter(mContext, mRadioText, mList.get(position).getTestId());
//            if (null != radioChildAdapter.getRadioBean()) {
//
//                radioinfo = radioChildAdapter.getRadioBean();
//                // setselectstr(radioChildAdapter.getSelectstr());
//
//            }
//            mViewHolderIssueStae.issueRadioList.setAdapter(radioChildAdapter);
        }

        //测试题单选
        if ("3".equals(type)) {
            if (null != rootview) {
                rootview = null;
                rootview = AnwserRadio(position, rootview);
            } else {
                rootview = AnwserRadio(position, rootview);
            }
        }
        //测试题多选
        if ("4".equals(type)) {
            if (null != rootview) {
                rootview = null;
                rootview = CheckBoxSelect(position, rootview, parent);
            } else {
                rootview = CheckBoxSelect(position, rootview, parent);
            }

        }
        //填空题
        if ("5".equals(type)) {
            if (null != rootview) {
                rootview = null;
                rootview = AreaTestQuestion(rootview, parent, position);
            } else {
                rootview = AreaTestQuestion(rootview, parent, position);
            }
        }

        return rootview;
    }

    @NonNull
    private View AreaTestQuestion(View view, ViewGroup parent, int position) {
        View mAreaLayoutView = null;
        ViewHolderArea holderArea;
        if (view == null) {
            mAreaLayoutView = mLayoutInflater.inflate(R.layout.listitem_area, parent, false);
            holderArea = new ViewHolderArea(mAreaLayoutView);
            mAreaLayoutView.setTag(holderArea);
            view = mAreaLayoutView;
        } else {

            holderArea = (ViewHolderArea) view.getTag();
        }
       if(null!=mList.get(position).getTaskRemark()&&mList.get(position).getTaskRemark().length()>0){

       }
        holderArea.mAreaQuestion.setText(mList.get(position).getTaskRemark());
//        holderArea.listItemAreaInput.setEtHint("请输入内容")//设置提示文字
//                .setEtMinHeight(200)//设置最小高度，单位px
//                .setLength(50)//设置总字数
//                .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
//                .setLineColor("#3F51B5")
//                .setContentId(mList.get(position).getTestId())
//                //.setContentId(id)
//                .show();

        holderArea.listItemAreaInput.setEtHint("请输入内容")//设置提示文字
                .setEtMinHeight(200)//设置最小高度，单位px
                .setLength(50)//设置总字数
                .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
                .setLineColor("#3F51B5")
                .setContentId(mList.get(position).getTestId())//设置横线颜色
                .show();

        if(!TextUtils.isEmpty(MyApplication.getInstance().getAreaAnwer())){
            holderArea.listItemAreaInput.setText(MyApplication.getInstance().getAreaAnwer());
        }



//        if(null!=mList.get(position).getReturnfile()){
//            String content=mList.get(position).getReturnfile().get(0);
//            if(null!=content)
//            {
////                holderArea.listItemAreaInput
////                        .setEtMinHeight(200)//设置最小高度，单位px
////                        .setLength(50)//设置总字数
////                        .setType(AnFQNumEditText.SINGULAR)//TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
////                        .setLineColor("#3F51B5")
////                        .setContentId(mList.get(position).getTestId())//设置横线颜色
////                        .show();
//                holderArea.listItemAreaInput.setText(content);
//            }else{
//                String textContent = holderArea.listItemAreaInput.getTextContent();
//                if (null == textContent) {
//                    textContent = "";
//                } else {
//                    Log.e("textContent", textContent.toString());
//                }
//
//                holderArea.listItemAreaInput.setText(textContent);
//            }
//
//
//        }

        return view;
    }

    //单选
    @NonNull
    private View AnwserRadio(final int position, View view) {
        View ViewRadio = null;
        ViewHolderRadio holder = null;
        if (view == null) {
            // ViewRadio = mLayoutInflater.inflate(R.layout.item_listview, null, false);
            ViewRadio = mLayoutInflater.inflate(R.layout.testanwersitem, null, false);
            holder = new ViewHolderRadio(ViewRadio);
            ViewRadio.setTag(holder);
            view = ViewRadio;

        } else {
            holder = (ViewHolderRadio) view.getTag();
        }

        holder.mSingleProblem.setText(mList.get(position).getTaskRemark());
        List<String> mRadioText = mList.get(position).getExampleFile();
        List<RadioButtonInfo> radioButtonInfos= mList.get(position).getRadioButtonInfos();
        RadioChildAdapter radioChildAdapter = new RadioChildAdapter(mContext, radioButtonInfos, mList.get(position).getTestId());
        if (null != radioChildAdapter.getRadioBean()) {

            radioinfo = radioChildAdapter.getRadioBean();
            // setselectstr(radioChildAdapter.getSelectstr());

        }
        holder.MyListView.setAdapter(radioChildAdapter);
        return view;
    }

    //多选 方法
    @NonNull
    private View CheckBoxSelect(int position, View view, ViewGroup parent) {
        View ViewCheckBox;
        ViewHolder viewHolder = null;

        if (view == null) {
            ViewCheckBox = LayoutInflater.from(mContext).inflate(R.layout.listparent_list, parent, false);
            viewHolder = new ViewHolder(ViewCheckBox);
            ViewCheckBox.setTag(viewHolder);
            view = ViewCheckBox;
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mTextViewProblem.setText(mList.get(position).getTaskRemark() + "");
        List<CheckBoxInfo> boxInfo = mList.get(position).getCheckBoxInfos();
        ChildAdapter childAdapter = new ChildAdapter(mContext, boxInfo);
        viewHolder.mListView.setAdapter(childAdapter);

        return view;
    }


    @NonNull
    private View ShowImageAndRecordVideo(final int position, View view, ViewGroup parent, String type) {
        View ViewImage = null;
        ViewHolderImage viewHolderImage = null;
        final int position1 = position;
        if (view == null) {
            ViewImage = mLayoutInflater.inflate(R.layout.listparent_imageitem, parent, false);
            viewHolderImage = new ViewHolderImage(ViewImage);
            ViewImage.setTag(viewHolderImage);
            view = ViewImage;
        } else {
            viewHolderImage = (ViewHolderImage) view.getTag();
        }
        // TaskInfoDetails.TaskListBean taskexampleinfo = mList.get(position1);
        LastTaskBean.TaskListBean taskexampleinfo = mList.get(position1);
        if ("0".equals(type)) {
            Log.e("type", type);
            viewHolderImage.mCameraPhoto.setVisibility(View.GONE);
        } else {
            viewHolderImage.mRecordVideo.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(taskexampleinfo.getExampleFile().get(0), viewHolderImage.mExamplePhoto, MyConfig.options);

        if(null!=taskexampleinfo.getReturnfile()&&taskexampleinfo.getReturnfile().size()>0){
            viewHolderImage.RootLayout.setVisibility(View.VISIBLE);
        }else{

            viewHolderImage.RootLayout.setVisibility(View.GONE);
        }

        //相机拍照
        viewHolderImage.mCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TaskObtainEvidenceTakePhoto(1);
                //  if (1 == whereindex) {
                // ((EvidenceFirst) mContext).TaskObtainEvidenceTakePhoto(EvidenceFirst.SYSTEM_TAKE_PHOTH_ONE, position1, mList.get(position).getSort());
                //  } else if (3 == whereindex) {
                ((LastTaskSaveAndUpLoad) mContext).TaskObtainEvidenceTakePhoto(LastTaskSaveAndUpLoad.SYSTEM_TAKE_PHOTH_ONE_LAST, position1, mList.get(position).getSort());
                //}
            }
        });

        //相机录像
        viewHolderImage.mRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (1 == whereindex) {
                //     ((EvidenceFirst) mContext).CameraVideoRcord(position1, mList.get(position).getSort());
                //  } else if (3 == whereindex) {
                ((LastTaskSaveAndUpLoad) mContext).CameraVideoRcord(LastTaskSaveAndUpLoad.VIDEO_CAPTURE_ONE_LAST,position1, mList.get(position).getSort());
                //   }

            }
        });
        //删除图片
        final ViewHolderImage finalViewHolderImage = viewHolderImage;
        viewHolderImage.mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((LastTaskSaveAndUpLoad) mContext).deleteShowCameraImage(mList.get(position).getSort());
                ((LastTaskSaveAndUpLoad) mContext).deleteShowCameraImage(String.valueOf(position1));
                list.remove(position);
               // finalViewHolderImage.RootLayout.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });

        if (null != list && list.size() > 0) {

            //   Log.e("partInfo.getPosition()","-------"+partInfo.getPosition());
            Log.e("position", "-------" + position);
            MutilPartInfo info = list.get(position1);
            if ((null != info && info.getPosition() == position1)) {

                //holder.mShowImage.setImageBitmap(dBitmap);
                viewHolderImage.RootLayout.setVisibility(View.VISIBLE);
                if ("0".equals(type)) {

                    //  Bitmap bitmap=  getVideoThumb2(info.getVideourl());
                    Log.e("info.getVideourl()", info.getThumbnail());
                    Bitmap dBitmap = BitmapFactory.decodeFile(info.getThumbnail());
                    viewHolderImage.mShowImage.setImageBitmap(dBitmap);
                    // ImageLoader.getInstance().displayImage("file://"+info.getVideourl(), holder.mShowImage, MyConfig.options);
                } else {
                    // viewHolderImage.mShowImage.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage("file://" + info.getUrl(), viewHolderImage.mShowImage, MyConfig.options);
                }

            } else {
                viewHolderImage.mShowImage.setImageBitmap(null);
                viewHolderImage.RootLayout.setVisibility(View.GONE);
            }
        } else {
            viewHolderImage.mShowImage.setImageBitmap(null);
            viewHolderImage.RootLayout.setVisibility(View.GONE);

        }
        return view;
    }


    @Override
    public void lunchcamera(Map<Integer, MutilPartInfo> list) {
        this.list = list;
    }


    //图片和视频  ViewHolder
    public class ViewHolderImage {
        //拍摄取证文本
        @BindView(R.id.taskevidence_image_step)
        TextView mTextContent;
        //拍摄需求
        @BindView(R.id.taskevidence_image_require)
        TextView Require;
        //例图
        @BindView(R.id.taskevidence_onlyexample)
        ImageView mExamplePhoto;
        //展示图片
        @BindView(R.id.taskevidence_show_onlyimage)
        ImageView mShowImage;
        //删除图片
        @BindView(R.id.taskevidence_delete_onlyimage)
        Button mDeleteImage;
        //照相机
        @BindView(R.id.taskevidence_image_camera)
        Button mCameraPhoto;
        @BindView(R.id.taskevidence_video_camera)
        Button mRecordVideo;

        @BindView(R.id.linearLayout_show_imageone)
        RelativeLayout RootLayout;

        ViewHolderImage(View view) {
            ButterKnife.bind(this, view);
        }
    }


    //测试题单选
    public class ViewHolderRadio {
        @BindView(R.id.test_single_problem)
        TextView mSingleProblem;
        @BindView(R.id.radio_list_parent)
        MyListView MyListView;
        @BindView(R.id.layout_backgroud_radio)
        LinearLayout mLayoutselectradius;

        ViewHolderRadio(View view) {
            ButterKnife.bind(this, view);
        }
    }


    static

    //多选  ViewHolder
    public class ViewHolder {
        @BindView(R.id.single_problem)
        TextView mTextViewProblem;
        @BindView(R.id.list_parent)
        MyListView mListView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public String getSelectstr() {
        return selectstr;
    }

    public void setSelectstr(String selectstr) {
        this.selectstr = selectstr;
    }


    public class ViewHolderArea {
        @BindView(R.id.list_item_area_input)
        AnFQNumEditText listItemAreaInput;
        @BindView(R.id.test_area_problem)
        TextView mAreaQuestion;

        ViewHolderArea(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public class ViewHolderIssueStae {
        @BindView(R.id.issue_single_problem)
        TextView issueSingleProblem;
        @BindView(R.id.issue_radio_list)
        MyListView issueRadioList;
        @BindView(R.id.layout_backgroud_radio)
        LinearLayout layoutBackgroudRadio;

        ViewHolderIssueStae(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public RadioBean getRadioinfo() {
        return radioinfo;
    }

    public void setRadioinfo(RadioBean radioinfo) {
        this.radioinfo = radioinfo;
    }



}
