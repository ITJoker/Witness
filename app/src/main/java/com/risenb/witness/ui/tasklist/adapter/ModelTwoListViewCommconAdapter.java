package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.beans.MutilPartInfo;
import com.risenb.witness.ui.tasklist.CheckBoxInfo;
import com.risenb.witness.ui.tasklist.EvidenceFirst;
import com.risenb.witness.ui.tasklist.InterImage;
import com.risenb.witness.ui.tasklist.ModelTwoExecEvidenceFirst;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModelTwoListViewCommconAdapter extends BaseAdapter implements InterImage {

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
    private boolean selected = true;
    private int whereindex = -1;

    //测试问题 和 图片视频
    private List<LastTaskBean.TaskListBean> mLastList;

    //图片和视频
    public ModelTwoListViewCommconAdapter(Context context, List<LastTaskBean.TaskListBean> mList, int index, List<String> strlist, String type) {

        this.mContext = context;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.whereindex = index;
//        if(null!=strlist){
//            this.radiostr=strlist;
//            this.radiostrType = type;
//        }

        if (2 == whereindex) {
            ((ModelTwoExecEvidenceFirst) mContext).setListener(this);
        }
    }

    //上刊状态 单选 选中的只
    public void setselectstr(String str) {
        this.selectstr = str;
        notifyDataSetChanged();
    }

    //上刊状态 单选 选中位置
    public void setSelectItem(int selectItem) {
        this.index = selectItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //图片和视频

        //单选
        if (1 == whereindex) {
            if (null != radiostr && !radiostr.isEmpty()) {
                return radiostr.size();
            }
        } else {
            //其他
            if (null != mList && !mList.isEmpty()) {
                return mList.size();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (1 == whereindex) {
            if (null != radiostr && !radiostr.isEmpty()) {
                return radiostr.get(position);
            }
        } else {
            if (null != mList && !mList.isEmpty()) {
                return mList.get(position);
            }
        }

        return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        String type;
        // if(null!=radiostrType){
        type = radiostrType;
        //}else{
        type = mList.get(position).getIsType();
        // }

        //图片和视频
        if ("1".equals(type) || "0".equals(type)) {
            view = ShowImageAndRecordVideo(position, view, parent, type);
        }


        //上刊状态单选
        if ("2".equals(type)) {
            view = IssuState(position, view);
        }

        //测试题单选
        if ("3".equals(type)) {

        }
        //测试题多选
        if ("4".equals(type)) {
            view = CheckBoxSelect(position, view, parent);
        }

        return view;
    }

    //上刊状态单选
    @NonNull
    private View IssuState(final int position, View view) {
        View ViewRadio;
        ViewHolderRadio holder;
        if (view == null) {
            ViewRadio = mLayoutInflater.inflate(R.layout.item_listview, null, false);
            holder = new ViewHolderRadio(ViewRadio);
            ViewRadio.setTag(holder);
            view = ViewRadio;

        } else {
            holder = (ViewHolderRadio) view.getTag();
        }

        if (null != radiostr) {
            holder.mRadioTextView.setText(radiostr.get(position));
        }

        if (selected == true && null != selectstr && null != radiostr && radiostr.get(position).equals(selectstr)) {
            index = position;
        }
        holder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    index = position;
                    notifyDataSetChanged();
                }
            }
        });

        if (index == position) {
            // 选中的条目和当前的条目是否相等
            holder.mRadioButton.setChecked(true);
            holder.mRootLayout.setBackgroundResource(R.drawable.task_situation_select);
            selected = false;
            setSelectstr(radiostr.get(position), false);
        } else {
            holder.mRadioButton.setChecked(false);
            holder.mRootLayout.setBackgroundResource(R.drawable.task_situation_normal);
        }
        return view;
    }

    //多选 方法
    @NonNull
    private View CheckBoxSelect(int position, View view, ViewGroup parent) {
        View ViewCheckBox;
        ViewHolder viewHolder;

        if (view == null) {
            ViewCheckBox = LayoutInflater.from(mContext).inflate(R.layout.listparent_list, parent, false);
            viewHolder = new ViewHolder(ViewCheckBox);
            ViewCheckBox.setTag(viewHolder);
            view = ViewCheckBox;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // mList.get(position);
        viewHolder.mTextViewProblem.setText(mList.get(position).getTaskRemark() + "");
        List<CheckBoxInfo> boxInfo = mList.get(position).getCheckBoxInfos();
        ChildAdapter childAdapter = new ChildAdapter(mContext, boxInfo);
        viewHolder.mListView.setAdapter(childAdapter);
        return view;
    }


    @NonNull
    private View ShowImageAndRecordVideo(final int position, View view, ViewGroup parent, String type) {
        View ViewImage;
        ViewHolderImage viewHolderImage;
        final int position1 = position;
//      final boolean isdelete=false;

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
            viewHolderImage.mRecordVideo.setVisibility(View.VISIBLE);
        } else {
            viewHolderImage.mRecordVideo.setVisibility(View.GONE);
            viewHolderImage.mCameraPhoto.setVisibility(View.VISIBLE);
        }

        ImageLoader.getInstance().displayImage(taskexampleinfo.getExampleFile().get(0), viewHolderImage.mExamplePhoto, MyConfig.options);
        if (null != taskexampleinfo.getReturnfile() && !taskexampleinfo.getReturnfile().isEmpty()) {
            viewHolderImage.mDeleteImage.setVisibility(View.VISIBLE);
            viewHolderImage.mShowImage.setVisibility(View.VISIBLE);
            viewHolderImage.RootLayout.setVisibility(View.VISIBLE);
            Log.e("taskexamplein", taskexampleinfo.getReturnfile().get(0) + "");
            ImageLoader.getInstance().displayImage(taskexampleinfo.getReturnfile().get(0), viewHolderImage.mShowImage, MyConfig.options);
        } else {
            viewHolderImage.mShowImage.setImageBitmap(null);
            viewHolderImage.mDeleteImage.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.please_upload_video, viewHolderImage.mShowImage, MyConfig.options);
        }

        //相机拍照
        viewHolderImage.mCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TaskObtainEvidenceTakePhoto(1);
                if (2 == whereindex) {
                    ((ModelTwoExecEvidenceFirst) mContext).TaskObtainEvidenceTakePhoto(EvidenceFirst.SYSTEM_TAKE_PHOTH_ONE, position1, mList.get(position).getSort());
                }
            }
        });

        //相机录像
        viewHolderImage.mRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (2 == whereindex) {
                    ((ModelTwoExecEvidenceFirst) mContext).CameraVideoRcord(position1, mList.get(position).getSort());
                }

            }
        });
        //删除图片
        final ViewHolderImage finalViewHolderImage = viewHolderImage;
        viewHolderImage.mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((ModelTwoEvidenceFirst) mContext).deleteShowCameraImage(mList.get(position).getSort());
                ((ModelTwoExecEvidenceFirst) mContext).deleteShowCameraImage(String.valueOf(position));
//                list.remove(position);
//               // finalViewHolderImage.RootLayout.setVisibility(View.GONE);
//                notifyDataSetChanged();
                if (!list.isEmpty()) {
                    list.remove(position);
                    // finalViewHolderImage1.RootLayout.setVisibility(View.GONE);
                    Log.e("remove local file", "删除当前文件");
                    //isremove=true;
                    notifyDataSetChanged();
                } else {
                    //  ExecTaskListInfo.TaskListBean  taskexampleinfo= mList.get(position1);
                    if (!mList.get(position1).getReturnfile().isEmpty()) {
                        List<String> returnfile = mList.get(position1).getReturnfile();
                        Log.e("returnfile", returnfile.toString());
                        mList.get(position1).getReturnfile().clear();
                        // finalViewHolderImage1.RootLayout.setVisibility(View.GONE);
                        Log.e("remove Http file", "删除当前文件");
                        //  isremove=true;
                        notifyDataSetChanged();
                    }
                }
            }
        });

        if (null != list && list.size() > 0) {

            //   Log.e("partInfo.getPosition()","-------"+partInfo.getPosition());
            Log.e("position", "-------" + position);
            MutilPartInfo info = list.get(position1);
            if ((null != info && info.getPosition() == position1)) {

                //holder.mShowImage.setImageBitmap(dBitmap);
                viewHolderImage.RootLayout.setVisibility(View.VISIBLE);
                viewHolderImage.mDeleteImage.setVisibility(View.VISIBLE);
                if ("0".equals(type)) {

                    //  Bitmap bitmap=  getVideoThumb2(info.getVideourl());
                    Log.e("info.getVideourl()", info.getThumbnail());
                    //本地拍摄视频的第一帧 截图  因为是本地图片 所以要加file://
                    ImageLoader.getInstance().displayImage("file://" + info.getThumbnail(), viewHolderImage.mShowImage, MyConfig.options);
                } else {
                    // viewHolderImage.mShowImage.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage("file://" + info.getUrl(), viewHolderImage.mShowImage, MyConfig.options);
                }

            } else {
                // viewHolderImage.mShowImage.setImageBitmap(null);
                // viewHolderImage.RootLayout.setVisibility(View.GONE);
            }
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


    //上刊状态单选
    public class ViewHolderRadio {
        @BindView(R.id.anwer_text_item)
        TextView mRadioTextView;
        @BindView(R.id.text_radio)
        RadioButton mRadioButton;
        @BindView(R.id.Layout_backgroud)
        RelativeLayout mRootLayout;

        ViewHolderRadio(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //多选  ViewHolder
    static public class ViewHolder {
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

    public void setSelectstr(String selectstr, boolean flag) {
        this.selectstr = selectstr;

    }

}
