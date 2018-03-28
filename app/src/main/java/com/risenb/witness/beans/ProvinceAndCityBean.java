package com.risenb.witness.beans;

import java.util.List;

public class ProvinceAndCityBean {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : [{"pid":"1","province_name":"北京市","city":[{"cid":"2","city_name":"北京市"}]},{"pid":"338","province_name":"天津市","city":[{"cid":"339","city_name":"天津市"}]},{"pid":"636","province_name":"河北省","city":[{"cid":"637","city_name":"石家庄市"},{"cid":"936","city_name":"唐山市"},{"cid":"1188","city_name":"秦皇岛市"},{"cid":"1291","city_name":"邯郸市"},{"cid":"1554","city_name":"邢台市"},{"cid":"1772","city_name":"保定市"},{"cid":"2142","city_name":"张家口市"},{"cid":"2400","city_name":"承德市"},{"cid":"2629","city_name":"沧州市"},{"cid":"2849","city_name":"廊坊市"},{"cid":"2968","city_name":"衡水市"}]},{"pid":"3102","province_name":"山西省","city":[{"cid":"3103","city_name":"太原市"},{"cid":"3224","city_name":"大同市"},{"cid":"3379","city_name":"阳泉市"},{"cid":"3431","city_name":"长治市"},{"cid":"3597","city_name":"晋城市"},{"cid":"3690","city_name":"朔州市"},{"cid":"3776","city_name":"晋中市"},{"cid":"3925","city_name":"运城市"},{"cid":"4093","city_name":"忻州市"},{"cid":"4304","city_name":"临汾市"},{"cid":"4494","city_name":"吕梁市"}]},{"pid":"4670","province_name":"内蒙古自治区","city":[{"cid":"4671","city_name":"呼和浩特市"},{"cid":"4759","city_name":"包头市"},{"cid":"4849","city_name":"乌海市"},{"cid":"4874","city_name":"赤峰市"},{"cid":"5029","city_name":"通辽市"},{"cid":"5162","city_name":"鄂尔多斯市"},{"cid":"5236","city_name":"呼伦贝尔市"},{"cid":"5418","city_name":"巴彦淖尔市"},{"cid":"5505","city_name":"乌兰察布市"},{"cid":"5616","city_name":"兴安盟"},{"cid":"5702","city_name":"锡林郭勒盟"},{"cid":"5799","city_name":"阿拉善盟"}]},{"pid":"5827","province_name":"辽宁省","city":[{"cid":"5828","city_name":"沈阳市"},{"cid":"6088","city_name":"大连市"},{"cid":"6266","city_name":"鞍山市"},{"cid":"6384","city_name":"抚顺市"},{"cid":"6476","city_name":"本溪市"},{"cid":"6542","city_name":"丹东市"},{"cid":"6643","city_name":"锦州市"},{"cid":"6771","city_name":"营口市"},{"cid":"6851","city_name":"阜新市"},{"cid":"6955","city_name":"辽阳市"},{"cid":"7024","city_name":"盘锦市"},{"cid":"7088","city_name":"铁岭市"},{"cid":"7208","city_name":"朝阳市"},{"cid":"7391","city_name":"葫芦岛市"}]},{"pid":"7531","province_name":"吉林省","city":[{"cid":"7532","city_name":"长春市"},{"cid":"7706","city_name":"吉林市"},{"cid":"7868","city_name":"四平市"},{"cid":"7986","city_name":"辽源市"},{"cid":"8037","city_name":"通化市"},{"cid":"8144","city_name":"白山市"},{"cid":"8216","city_name":"松原市"},{"cid":"8333","city_name":"白城市"},{"cid":"8445","city_name":"延边朝鲜族自治州"}]},{"pid":"8558","province_name":"黑龙江省","city":[{"cid":"8559","city_name":"哈尔滨市"},{"cid":"8884","city_name":"齐齐哈尔市"},{"cid":"9117","city_name":"鸡西市"},{"cid":"9222","city_name":"鹤岗市"},{"cid":"9296","city_name":"双鸭山市"},{"cid":"9419","city_name":"大庆市"},{"cid":"9553","city_name":"伊春市"},{"cid":"9785","city_name":"佳木斯市"},{"cid":"9930","city_name":"七台河市"},{"cid":"9981","city_name":"牡丹江市"},{"cid":"10084","city_name":"黑河市"},{"cid":"10252","city_name":"绥化市"},{"cid":"10483","city_name":"大兴安岭地区"}]},{"pid":"10543","province_name":"上海市","city":[{"cid":"10544","city_name":"上海市"}]},{"pid":"10808","province_name":"江苏省","city":[{"cid":"10809","city_name":"南京市"},{"cid":"10960","city_name":"无锡市"},{"cid":"11067","city_name":"徐州市"},{"cid":"11245","city_name":"常州市"},{"cid":"11348","city_name":"苏州市"},{"cid":"11482","city_name":"南通市"},{"cid":"11663","city_name":"连云港市"},{"cid":"11786","city_name":"淮安市"},{"cid":"11947","city_name":"盐城市"},{"cid":"12135","city_name":"扬州市"},{"cid":"12249","city_name":"镇江市"},{"cid":"12343","city_name":"泰州市"},{"cid":"12475","city_name":"宿迁市"}]},{"pid":"12596","province_name":"浙江省","city":[{"cid":"12597","city_name":"杭州市"},{"cid":"12813","city_name":"宁波市"},{"cid":"12974","city_name":"温州市"},{"cid":"13280","city_name":"嘉兴市"},{"cid":"13364","city_name":"湖州市"},{"cid":"13437","city_name":"绍兴市"},{"cid":"13564","city_name":"金华市"},{"cid":"13726","city_name":"衢州市"},{"cid":"13840","city_name":"舟山市"},{"cid":"13890","city_name":"台州市"},{"cid":"14033","city_name":"丽水市"}]},{"pid":"14234","province_name":"安徽省","city":[{"cid":"14235","city_name":"合肥市"},{"cid":"14351","city_name":"芜湖市"},{"cid":"14410","city_name":"蚌埠市"},{"cid":"14500","city_name":"淮南市"},{"cid":"14575","city_name":"马鞍山市"},{"cid":"14612","city_name":"淮北市"},{"cid":"14653","city_name":"铜陵市"},{"cid":"14687","city_name":"安庆市"},{"cid":"14887","city_name":"黄山市"},{"cid":"15005","city_name":"滁州市"},{"cid":"15194","city_name":"阜阳市"},{"cid":"15378","city_name":"宿州市"},{"cid":"15499","city_name":"巢湖市"},{"cid":"15586","city_name":"六安市"},{"cid":"15764","city_name":"亳州市"},{"cid":"15871","city_name":"池州市"},{"cid":"15958","city_name":"宣城市"}]},{"pid":"16068","province_name":"福建省","city":[{"cid":"16069","city_name":"福州市"},{"cid":"16278","city_name":"厦门市"},{"cid":"16348","city_name":"莆田市"},{"cid":"16412","city_name":"三明市"},{"cid":"16572","city_name":"泉州市"},{"cid":"16754","city_name":"漳州市"},{"cid":"16924","city_name":"南平市"},{"cid":"17077","city_name":"龙岩市"},{"cid":"17219","city_name":"宁德市　"}]},{"pid":"17359","province_name":"江西省","city":[{"cid":"17360","city_name":"南昌市"},{"cid":"17508","city_name":"景德镇市"},{"cid":"17589","city_name":"萍乡市"},{"cid":"17651","city_name":"九江市"},{"cid":"17894","city_name":"新余市"},{"cid":"17934","city_name":"鹰潭市"},{"cid":"17999","city_name":"赣州市"},{"cid":"18330","city_name":"吉安市"},{"cid":"18598","city_name":"宜春市"},{"cid":"18829","city_name":"抚州市"},{"cid":"19024","city_name":"上饶市"}]},{"pid":"19280","province_name":"山东省","city":[{"cid":"19281","city_name":"济南市"},{"cid":"19421","city_name":"青岛市"},{"cid":"19608","city_name":"淄博市"},{"cid":"19724","city_name":"枣庄市"},{"cid":"19796","city_name":"东营市"},{"cid":"19846","city_name":"烟台市"},{"cid":"20012","city_name":"潍坊市"},{"cid":"20216","city_name":"济宁市"},{"cid":"20386","city_name":"泰安市"},{"cid":"20480","city_name":"威海市"},{"cid":"20558","city_name":"日照市"},{"cid":"20618","city_name":"莱芜市"},{"cid":"20642","city_name":"临沂市"},{"cid":"20836","city_name":"德州市"},{"cid":"20981","city_name":"聊城市"},{"cid":"21123","city_name":"滨州市"},{"cid":"21218","city_name":"菏泽市"}]},{"pid":"21387","province_name":"河南省","city":[{"cid":"21388","city_name":"郑州市"},{"cid":"21575","city_name":"开封市"},{"cid":"21711","city_name":"洛阳市"},{"cid":"21913","city_name":"平顶山市"},{"cid":"22058","city_name":"安阳市"},{"cid":"22206","city_name":"鹤壁市"},{"cid":"22251","city_name":"新乡市"},{"cid":"22423","city_name":"焦作市"},{"cid":"22558","city_name":"濮阳市"},{"cid":"22655","city_name":"许昌市"},{"cid":"22762","city_name":"漯河市"},{"cid":"22824","city_name":"三门峡市"},{"cid":"22910","city_name":"南阳市"},{"cid":"23170","city_name":"商丘市"},{"cid":"23372","city_name":"信阳市"},{"cid":"23589","city_name":"周口市"},{"cid":"23818","city_name":"驻马店市"}]},{"pid":"24022","province_name":"湖北省","city":[{"cid":"24023","city_name":"武汉市"},{"cid":"24224","city_name":"黄石市"},{"cid":"24291","city_name":"十堰市"},{"cid":"24453","city_name":"宜昌市"},{"cid":"24580","city_name":"襄樊市"},{"cid":"24706","city_name":"鄂州市"},{"cid":"24737","city_name":"荆门市"},{"cid":"24816","city_name":"孝感市"},{"cid":"24949","city_name":"荆州市"},{"cid":"25086","city_name":"黄冈市"},{"cid":"25249","city_name":"咸宁市"},{"cid":"25335","city_name":"随州市"},{"cid":"25388","city_name":"恩施州"},{"cid":"25487","city_name":"湖南省直辖市"}]},{"pid":"25579","province_name":"湖南省","city":[{"cid":"25580","city_name":"长沙市"},{"cid":"25758","city_name":"株洲市"},{"cid":"25912","city_name":"湘潭市"},{"cid":"26001","city_name":"衡阳市"},{"cid":"26242","city_name":"邵阳市"},{"cid":"26485","city_name":"岳阳市"},{"cid":"26683","city_name":"常德市"},{"cid":"26925","city_name":"张家界市"},{"cid":"27038","city_name":"益阳市"},{"cid":"27147","city_name":"郴州市"},{"cid":"27418","city_name":"永州市"},{"cid":"27650","city_name":"怀化市"},{"cid":"27963","city_name":"娄底市"},{"cid":"28065","city_name":"湘西土家族苗族自治州"}]},{"pid":"28240","province_name":"广东省","city":[{"cid":"28241","city_name":"广州市"},{"cid":"28421","city_name":"韶关市"},{"cid":"28558","city_name":"深圳市"},{"cid":"28626","city_name":"珠海市"},{"cid":"28659","city_name":"汕头市"},{"cid":"28737","city_name":"佛山市"},{"cid":"28785","city_name":"江门市"},{"cid":"28880","city_name":"湛江市"},{"cid":"29026","city_name":"茂名市"},{"cid":"29159","city_name":"肇庆市"},{"cid":"29282","city_name":"惠州市"},{"cid":"29371","city_name":"梅州市"},{"cid":"29498","city_name":"汕尾市"},{"cid":"29568","city_name":"河源市"},{"cid":"29679","city_name":"阳江市"},{"cid":"29755","city_name":"清远市"},{"cid":"29855","city_name":"东莞市"},{"cid":"29890","city_name":"中山市"},{"cid":"29915","city_name":"潮州市"},{"cid":"29977","city_name":"揭阳市"},{"cid":"30086","city_name":"云浮市"}]},{"pid":"30164","province_name":"广西壮族自治区","city":[{"cid":"30165","city_name":"南宁市"},{"cid":"30319","city_name":"柳州市"},{"cid":"30448","city_name":"桂林市"},{"cid":"30613","city_name":"梧州市"},{"cid":"30688","city_name":"北海市"},{"cid":"30724","city_name":"防城港市"},{"cid":"30762","city_name":"钦州市"},{"cid":"30834","city_name":"贵港市"},{"cid":"30915","city_name":"玉林市"},{"cid":"31033","city_name":"百色市"},{"cid":"31184","city_name":"贺州市"},{"cid":"31249","city_name":"河池市"},{"cid":"31401","city_name":"来宾市"},{"cid":"31478","city_name":"崇左市"}]},{"pid":"31563","province_name":"海南省","city":[{"cid":"31564","city_name":"海口市"},{"cid":"31618","city_name":"三亚市"},{"cid":"31633","city_name":"海南省直属市"}]},{"pid":"31929","province_name":"重庆市","city":[{"cid":"47544","city_name":"重庆市"}]},{"pid":"33007","province_name":"四川省","city":[{"cid":"33008","city_name":"成都市"},{"cid":"33344","city_name":"自贡市"},{"cid":"33460","city_name":"攀枝花市"},{"cid":"33528","city_name":"泸州市"},{"cid":"33681","city_name":"德阳市"},{"cid":"33817","city_name":"绵阳市"},{"cid":"34120","city_name":"广元市"},{"cid":"34376","city_name":"遂宁市"},{"cid":"34501","city_name":"内江市"},{"cid":"34628","city_name":"乐山市"},{"cid":"34859","city_name":"南充市"},{"cid":"35288","city_name":"眉山市"},{"cid":"35427","city_name":"宜宾市"},{"cid":"35625","city_name":"广安市"},{"cid":"35813","city_name":"达州市"},{"cid":"36136","city_name":"雅安市"},{"cid":"36299","city_name":"巴中市"},{"cid":"36497","city_name":"资阳市"},{"cid":"36679","city_name":"阿坝州"},{"cid":"36926","city_name":"甘孜藏族自治州"},{"cid":"37270","city_name":"凉山州"}]},{"pid":"37906","province_name":"贵州省","city":[{"cid":"37907","city_name":"贵阳市"},{"cid":"38042","city_name":"六盘水市"},{"cid":"38145","city_name":"遵义市"},{"cid":"38402","city_name":"安顺市"},{"cid":"38497","city_name":"铜仁地区"},{"cid":"38677","city_name":"黔西南州"},{"cid":"38816","city_name":"毕节地区"},{"cid":"39075","city_name":"黔东南苗族侗族自治州"},{"cid":"39302","city_name":"黔南布依族苗族自治州"}]},{"pid":"39556","province_name":"云南省","city":[{"cid":"39557","city_name":"昆明市"},{"cid":"39710","city_name":"曲靖市"},{"cid":"39836","city_name":"玉溪市"},{"cid":"39923","city_name":"保山市"},{"cid":"40004","city_name":"昭通市"},{"cid":"40160","city_name":"丽江市"},{"cid":"40230","city_name":"思茅市"},{"cid":"40348","city_name":"临沧市"},{"cid":"40441","city_name":"楚雄州"},{"cid":"40555","city_name":"红河州"},{"cid":"40705","city_name":"文山州"},{"cid":"40816","city_name":"西双版纳州"},{"cid":"40852","city_name":"大理州"},{"cid":"40979","city_name":"德宏州"},{"cid":"41036","city_name":"怒江州"},{"cid":"41070","city_name":"迪庆州"}]},{"pid":"41103","province_name":"西藏自治区","city":[{"cid":"41104","city_name":"拉萨市"},{"cid":"41178","city_name":"昌都地区"},{"cid":"41328","city_name":"山南地区"},{"cid":"41423","city_name":"日喀则地区"},{"cid":"41645","city_name":"那曲地区"},{"cid":"41770","city_name":"阿里地区"},{"cid":"41814","city_name":"林芝地区"}]},{"pid":"41877","province_name":"陕西省","city":[{"cid":"41878","city_name":"西安市"},{"cid":"42069","city_name":"铜川市"},{"cid":"42119","city_name":"宝鸡市"},{"cid":"42287","city_name":"咸阳市"},{"cid":"42490","city_name":"渭南市"},{"cid":"42703","city_name":"延安市"},{"cid":"42888","city_name":"汉中市"},{"cid":"43136","city_name":"榆林市"},{"cid":"43379","city_name":"安康市"},{"cid":"43592","city_name":"商洛市"}]},{"pid":"43776","province_name":"甘肃省","city":[{"cid":"43777","city_name":"兰州市"},{"cid":"43904","city_name":"嘉峪关市"},{"cid":"43914","city_name":"金昌市"},{"cid":"43936","city_name":"白银市"},{"cid":"44022","city_name":"天水市"},{"cid":"44154","city_name":"武威市"},{"cid":"44265","city_name":"张掖市"},{"cid":"44352","city_name":"平凉市"},{"cid":"44477","city_name":"酒泉市"},{"cid":"44569","city_name":"庆阳市"},{"cid":"44699","city_name":"定西市"},{"cid":"44829","city_name":"陇南市"},{"cid":"45035","city_name":"临夏州"},{"cid":"45174","city_name":"甘南州"}]},{"pid":"45286","province_name":"青海省","city":[{"cid":"45287","city_name":"西宁市"},{"cid":"45368","city_name":"海东地区"},{"cid":"45471","city_name":"海北州"},{"cid":"45510","city_name":"黄南州"},{"cid":"45548","city_name":"海南州"},{"cid":"45597","city_name":"果洛州"},{"cid":"45648","city_name":"玉树州"},{"cid":"45701","city_name":"海西州"}]},{"pid":"45753","province_name":"宁夏回族自治区","city":[{"cid":"45754","city_name":"银川市"},{"cid":"45825","city_name":"石嘴山市"},{"cid":"45871","city_name":"吴忠市"},{"cid":"45926","city_name":"固原市"},{"cid":"45999","city_name":"中卫市"}]},{"pid":"46047","province_name":"新疆维吾尔自治区","city":[{"cid":"46048","city_name":"乌鲁木齐市"},{"cid":"46138","city_name":"克拉玛依市"},{"cid":"46162","city_name":"吐鲁番地区"},{"cid":"46197","city_name":"哈密地区"},{"cid":"46255","city_name":"昌吉州"},{"cid":"46380","city_name":"博尔塔拉蒙古自治州"},{"cid":"46422","city_name":"巴音郭楞蒙古自治州"},{"cid":"46551","city_name":"阿克苏地区"},{"cid":"46688","city_name":"克州"},{"cid":"46747","city_name":"喀什地区"},{"cid":"46957","city_name":"和田地区"},{"cid":"47069","city_name":"伊犁州"},{"cid":"47241","city_name":"塔城地区"},{"cid":"47374","city_name":"阿勒泰地区"},{"cid":"47450","city_name":"新疆维吾尔自治区省直辖市"}]},{"pid":"47493","province_name":"台湾省","city":[]},{"pid":"47494","province_name":"香港特别行政区","city":[]},{"pid":"47495","province_name":"澳门特别行政区","city":[]}]
     */

    private int success;
    private String errorMsg;
    private List<DataBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pid : 1
         * province_name : 北京市
         * city : [{"cid":"2","city_name":"北京市"}]
         */

        private String pid;
        private String province_name;
        private List<CityBean> city;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }

        public static class CityBean {
            /**
             * cid : 2
             * city_name : 北京市
             */

            private String cid;
            private String city_name;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }
        }
    }
}
