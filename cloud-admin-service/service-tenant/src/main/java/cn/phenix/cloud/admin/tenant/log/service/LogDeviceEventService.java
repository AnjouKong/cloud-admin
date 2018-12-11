//package cn.phenix.cloud.admin.tenant.log.service;
//
//import cn.phenix.cloud.admin.tenant.log.dao.LogDeviceEventDao;
//import cn.phenix.model.log.LogDeviceEvent;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//
///**
// * @author xiaobin
// * @create 2017-09-21 下午12:48
// **/
//@Service
//public class LogDeviceEventService extends BaseMongoBiz<LogDeviceEvent, LogDeviceEventDao> {
//
//    @Autowired
//    private LogDeviceEventDao logDeviceEventDao;
//
//    //点播视频数
//    public String videoPlayNum(Date date){
//
//        if(date == null ||date.equals("")){
//            date= new Date();
//        }
//        Query query = new Query();
//        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd 00:00:00 0000");
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);   //设置当前日期
//        c.add(Calendar.DATE, -1); //日期加1,Calendar.DATE(天),Calendar.HOUR(小时)
//        Date dd = c.getTime(); //结果
//
//        String today = sdf.format(date).toString();
//        String yesterDay = sdf.format(dd).toString();
//
//        Criteria criteria=Criteria.where("eventEnum").is("MEDIA_DETAIL_EVENT").and("startTime").gte(yesterDay).lt(today);
//        List<LogDeviceEvent> videoPlay = logDeviceEventDao.findByQuery(query.addCriteria(criteria));
//
//        return String.valueOf(videoPlay.size());
//    }
//
//    //点播设备数
//    public String playDeviceNum(Date date){
//        if(date == null ||date.equals("")){
//            date= new Date();
//        }
//
//        Query query = new Query();
//        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd 00:00:00 0000");
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);   //设置当前日期
//        c.add(Calendar.DATE, -1); //日期加1,Calendar.DATE(天),Calendar.HOUR(小时)
//        Date dd = c.getTime(); //结果
//
//        String today = sdf.format(date).toString();
//        String yesterDay = sdf.format(dd).toString();
//
//        Criteria criteria=Criteria.where("eventEnum").is("MEDIA_DETAIL_EVENT").and("startTime").gte(yesterDay).lt(today);
//        List<LogDeviceEvent> videoPlay = logDeviceEventDao.findByQuery(query.addCriteria(criteria));
//
//        HashSet set = new HashSet();
//
//        //去重
//        for(int i=0;i<videoPlay.size();i++){
//            if(videoPlay.get(i).getDeviceInfo() != null){
//                set.add(videoPlay.get(i).getDeviceInfo().getDeviceId());
//            }
//        }
//
//        return  String.valueOf(set.size());
//    }
//
//    //人均点播数
//    public String averagePlay(Date date){
//        return null;
//    }
//    //点播率
//    public String videoPlayRate(long pubSeries,Date date){
//        if(date == null ||date.equals("")){
//            date= new Date();
//        }
//        Query query = new Query();
//        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd 00:00:00 0000");
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);   //设置当前日期
//        c.add(Calendar.DATE, -1); //日期加1,Calendar.DATE(天),Calendar.HOUR(小时)
//        Date dd = c.getTime(); //结果
//
//        String today = sdf.format(date).toString();
//        String yesterDay = sdf.format(dd).toString();
//
//        Criteria criteria=Criteria.where("eventEnum").is("MEDIA_DETAIL_EVENT").and("startTime").gte(yesterDay).lt(today);
//        List<LogDeviceEvent> videoPlay = logDeviceEventDao.findByQuery(query.addCriteria(criteria));
//
//
//        HashSet set = new HashSet();
//
//        //播放媒资去重
//        for(int i=0;i<videoPlay.size();i++){
//            if(videoPlay.get(i).getResourceId() != null){
//                set.add(videoPlay.get(i).getResourceId());
//            }
//        }
//        //发布媒资总数pubSeries
//        NumberFormat numberFormat = NumberFormat.getInstance();
//        numberFormat.setMaximumFractionDigits(2);
//
//        float rate = ((float) set.size()/(float) pubSeries) * 100;
//        String result = numberFormat.format(rate)+"%";
//
//        return result;
//    }
//
//}
