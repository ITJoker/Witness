package com.risenb.witness.ui.tasklist.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.risenb.witness.beans.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class DaoHelper {
    private SQLiteHelper helper;
    private SQLiteDatabase db;

    public DaoHelper(Context context) {
        helper = SQLiteHelper.Instance(context);
        db = helper.getWritableDatabase();
    }

    /**
     * taskid        任务ID
     * path          视频路径
     * thumbnailpath 缩略图路径
     * isupload      是否上传过 0 没上传 1上传过
     */
    public int insertVideoInfo(String taskid, String path, String thumbnailpath, String isupload, String page, String sort, String position) {
        long returnvalues = -1;
        try {
            // insert一条数据
            String sql = "insert into videoinfo(taskid,path,page,sort,thumbnailpath,isupload) values(" + taskid + "," + path + "," + page + "," + sort + "," + thumbnailpath + "," + isupload + "," + position + ")";
            Log.e("Mysql", sql);
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //使用ContentValues添加数据
            values.put("taskid", taskid);
            values.put("path", path);
            if (null != thumbnailpath) {
                values.put("thumbnailpath", thumbnailpath);
            }
            values.put("page", page);
            values.put("sort", sort);
            values.put("isupload", isupload);
            values.put("position", position);
            returnvalues = db.insert("videoinfo", null, values);
            // 执行SQL 这种方式没有返回值
            //database.execSQL(sql);
            Log.e("returnvalues", returnvalues + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        return (int) returnvalues;
    }

    /**
     * 修改数据库某一条数据
     * @param taskid        任务ID
     * @param path          图片路径或者视频
     * @param thumbnailpath 缩略图路径
     * @param page          那一页
     * @param sort          位置
     */
    public int modifyVideoInfo(String taskid, String path, String thumbnailpath, String page, String sort) {
        long returnvalues = -1;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("path", path);

            if (null != thumbnailpath) {
                values.put("thumbnailpath", thumbnailpath);
            }
            return db.update("videoinfo", values, "taskid=? and page=? and sort=?", new String[]{taskid, page, sort});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        return (int) returnvalues;
    }

    public int finishVideoInfo(String taskid, String path, String page, String sort, String isupload) {
        long returnvalues = -1;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("isupload", isupload);
            return db.update("videoinfo", values, "taskid=? and page=? and path=? and sort=?", new String[]{taskid, page, path, sort});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        return (int) returnvalues;
    }

    public VideoInfo queryVideoInfo(String taskid, String page, String sort) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cursor = db.query("videoinfo", new String[]{"taskid", "path", "thumbnailpath", "page", "sort", "isupload", "position"}, "taskid=? and page=? and sort=?", new String[]{taskid, page, sort}, null, null, null, null);
            //List<VideoInfo> list=new ArrayList<>();
            //注意返回结果有可能为空
            VideoInfo info = null;
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex("taskid"));
                String pathvideo = cursor.getString(cursor.getColumnIndex("path"));
                String videothumbnailpath = cursor.getString(cursor.getColumnIndex("thumbnailpath"));
                String pages = cursor.getString(cursor.getColumnIndex("page"));
                String sorts = cursor.getString(cursor.getColumnIndex("sort"));
                String isuploads = cursor.getString(cursor.getColumnIndex("isupload"));
                String positions = cursor.getString(cursor.getColumnIndex("position"));
                // Log.e("databaseinfo",id+"--"+pathvideo+"--"+videothumbnailpath+"--"+isuploads);
                info = new VideoInfo(id, pathvideo, videothumbnailpath, sorts, pages, isuploads, positions);
                //list.add(info);
            }
            // Log.e(" List<VideoInfo>",info.toString());
            cursor.close();
            return info;
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        //Cursor对象返回查询结果
        return null;
    }

    public List<VideoInfo> queryVideotask(String taskid, String upload, String page) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            List<VideoInfo> list = new ArrayList<>();
            Cursor cursor = db.query("videoinfo", new String[]{"taskid", "path", "thumbnailpath", "page", "sort", "isupload", "position"}, "taskid=? and isupload=? and page=?", new String[]{taskid, upload, page}, null, null, null, null);
            //List<VideoInfo> list=new ArrayList<>();
            //注意返回结果有可能为空
            VideoInfo info;
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("taskid"));
                String pathvideo = cursor.getString(cursor.getColumnIndex("path"));
                String videothumbnailpath = cursor.getString(cursor.getColumnIndex("thumbnailpath"));
                String pages = cursor.getString(cursor.getColumnIndex("page"));
                String sorts = cursor.getString(cursor.getColumnIndex("sort"));
                String isuploads = cursor.getString(cursor.getColumnIndex("isupload"));
                String positions = cursor.getString(cursor.getColumnIndex("position"));
                info = new VideoInfo(id, pathvideo, videothumbnailpath, sorts, pages, isuploads, positions);
                list.add(info);
            }
            cursor.close();
            return list;
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        //Cursor对象返回查询结果
        return null;
    }

    /**
     * 通过ID查询当前没有上传的任务
     */
    public List<VideoInfo> queryVideoInfoAll(String taskid, String isupload) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.e("databaseinfo", taskid + "--" + isupload);
        try {
            Cursor cursor = db.query("videoinfo", new String[]{"taskid", "path", "thumbnailpath", "page", "sort", "isupload", "position"}, "taskid=? and isupload=?", new String[]{taskid, isupload}, null, null, null, null);
            List<VideoInfo> list = new ArrayList<>();
            //注意返回结果有可能为空
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("taskid"));
                String pathvideo = cursor.getString(cursor.getColumnIndex("path"));
                String videothumbnailpath = cursor.getString(cursor.getColumnIndex("thumbnailpath"));
                String pages = cursor.getString(cursor.getColumnIndex("page"));
                String sorts = cursor.getString(cursor.getColumnIndex("sort"));
                String isuploads = cursor.getString(cursor.getColumnIndex("isupload"));
                String positions = cursor.getString(cursor.getColumnIndex("position"));
                Log.e("databaseinfo", id + "--" + pathvideo + "--" + videothumbnailpath + "--" + isuploads);
                VideoInfo info = new VideoInfo(id, pathvideo, videothumbnailpath, sorts, pages, isuploads, positions);
                list.add(info);
            }
            Log.e(" List<VideoInfo>", list.toString());
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    public List<VideoInfo> queryVideotask(String upload) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            List<VideoInfo> list = new ArrayList<>();
            Cursor cursor = db.query("videoinfo", new String[]{"taskid", "path", "thumbnailpath", "page", "sort", "isupload", "position"}, "isupload=?", new String[]{upload}, null, null, null, null);
            //List<VideoInfo> list=new ArrayList<>();
            //注意返回结果有可能为空
            VideoInfo info;
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex("taskid"));
                String pathvideo = cursor.getString(cursor.getColumnIndex("path"));
                String videothumbnailpath = cursor.getString(cursor.getColumnIndex("thumbnailpath"));
                String pages = cursor.getString(cursor.getColumnIndex("page"));
                String sorts = cursor.getString(cursor.getColumnIndex("sort"));
                String isuploads = cursor.getString(cursor.getColumnIndex("isupload"));
                String positions = cursor.getString(cursor.getColumnIndex("position"));
                // Log.e("databaseinfo",id+"--"+pathvideo+"--"+videothumbnailpath+"--"+isuploads);

                info = new VideoInfo(id, pathvideo, videothumbnailpath, sorts, pages, isuploads, positions);
                list.add(info);
            }
            Log.e(" List<VideoInfo>", list.toString());
            cursor.close();
            return list;
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
        //Cursor对象返回查询结果

        return null;
    }

    /**
     * 查询已经保存的任务
     */
    public List<VideoInfo> queryAlltask(String finish) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cursor = db.query("videoinfo", new String[]{"taskid", "path", "thumbnailpath", "page", "sort", "isupload", "finish", "position"}, "finish=?", new String[]{finish}, null, null, null, null);
            List<VideoInfo> list = new ArrayList<>();
            //注意返回结果有可能为空
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("taskid"));
                String pathvideo = cursor.getString(cursor.getColumnIndex("path"));
                String videothumbnailpath = cursor.getString(cursor.getColumnIndex("thumbnailpath"));
                String pages = cursor.getString(cursor.getColumnIndex("page"));
                String sorts = cursor.getString(cursor.getColumnIndex("sort"));
                String isuploads = cursor.getString(cursor.getColumnIndex("isupload"));
                String finished = cursor.getString(cursor.getColumnIndex("finish"));
                String positions = cursor.getString(cursor.getColumnIndex("position"));
                Log.e("databaseinfo", id + "--" + pathvideo + "--" + videothumbnailpath + "--" + isuploads);
                VideoInfo info = new VideoInfo(id, pathvideo, videothumbnailpath, sorts, pages, isuploads, positions);
                list.add(info);
            }
            Log.e(" List<VideoInfo>", list.toString());
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    public void Close() {
        if (null != db) {
            db.close();
        }
        if (null != helper) {
            helper.close();
        }
    }

    public void delete(String taskid, String path) {
        db = helper.getWritableDatabase();
        //删除条件
        String whereClause = "taskid=? and path=?";
        //删除条件参数
        String[] whereArgs = {taskid, path};
        int deletecount = db.delete("videoinfo", whereClause, whereArgs);
    }

}
