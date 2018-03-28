package com.risenb.witness.ui.tasklist;

import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.beans.MultimediaInfo;

import java.util.HashMap;
import java.util.List;

public class EventMessage {

    public static class FirstMessage{
        private String name;
        private String message;

        public FirstMessage(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Mesage{

        public String message;
        public String id;
        public Mesage(String message,String id) {
            this.message = message;
            this.id=id;
        }
    }
    public static class MesageEmpty{

        public String message;
        public String id;
        public MesageEmpty(String message,String id) {
            this.message = message;
            this.id=id;
        }
    }

    public static class MesageCheckBox{

        public HashMap<Integer, Boolean> isSelected;
        public String id;
        public MesageCheckBox(HashMap<Integer, Boolean> isSelected,String id) {
            this.isSelected = isSelected;
            this.id=id;
        }
    }

    public static class MesageMultimediaInfo{

        public MultimediaInfo info;
        public MesageMultimediaInfo(MultimediaInfo info) {
            this.info = info;
        }
    }

    public static class MesageAnswer{

        public List<ExecTaskInfo.AnswerBean> info;
        public MesageAnswer(List<ExecTaskInfo.AnswerBean> info) {
            this.info = info;
        }
    }

}
