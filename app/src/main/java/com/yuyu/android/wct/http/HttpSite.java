package com.yuyu.android.wct.http;

/**
 * Created by bernie.shi on 2016/3/23.
 */
public class HttpSite {
    public static String httpsuffix = ".do";

    //测试环境
    public static String httpPoint = "https://cloud888.yuyutechnology.com/bdls/track/";
    public static String httpSite = "http://www888.lotus.club/wishcometrueserver/";//基础服务器域名
    public static String videoPath_qiniu = "http://7xou2a.com1.z0.glb.clouddn.com/crm_res/movies/";//拼接七牛视频域名
    public static String imagePath_qiniu = "http://7xou2a.com1.z0.glb.clouddn.com/crm_res/imgs/";//拼接七牛视频图片域名
    public static String homePageH5 = "http://m888.lotus.club/html5/h5/Winner.html";//获奖公告H5域名
    public static String awardsResultsH5 = "http://m888.lotus.club/html5/h5/AwardDetail.html";//获奖详情H5域名
    public static String rulePageH5 = "http://m888.lotus.club/html5/h5/Rules.html";//规则H5域名
    public static String trailerPageH5 = "http://m888.lotus.club/html5/h5/trailer.html";//预告页H5域名
    public static final String registerSite = "http://m888.lotus.club/wishcometrue/register";
    public static final String bugTickets = "http://m888.lotus.club/wishcometrue/shopping/about_movie";
    public static String homePageWebViewH5 = "http://172.18.188.222:8080/wctphone/winner";

    //生产环境
//    public static String httpPoint = "https://cloud.yuyutechnology.com/bdls/track/";
//    public static String httpSite = "http://www.lotus.club/wishcometrueserver/";//基础服务器域名
//    public static String videoPath_qiniu = "http://7xoz33.com1.z0.glb.clouddn.com/crm_res/movies/";//拼接七牛视频域名
//    public static String imagePath_qiniu = "http://7xoz33.com1.z0.glb.clouddn.com/crm_res/imgs/";//拼接七牛视频图片域名
//    public static String homePageH5 = "http://m.lotus.club/html5/h5/Winner.html";//获奖公告H5域名
//    public static String awardsResultsH5 = "http://m.lotus.club/html5/h5/AwardDetail.html";//获奖详情H5域名
//    public static String rulePageH5 = "http://m.lotus.club/html5/h5/Rules.html";//规则H5域名
//    public static String trailerPageH5 = "http://m.lotus.club/html5/h5/trailer.html";//预告页H5域名
//    public static final String registerSite = "http://m.lotus.club/wishcometrue/register";
//    public static final String bugTickets = "http://m.lotus.club/wishcometrue/shopping/about_movie";
//    public static final String homePageWebViewH5 = "";

    public static String loginSite = httpSite + "user/login" + httpsuffix;//登录接口
    public static String voteSite = httpSite + "user/vote"+ httpsuffix;//投票接口
    public static String videoResultSite = httpSite + "video/getVideoByActId"+ httpsuffix;//获得当前期视频详情接口
    public static String videoVoteResult = httpSite + "video/voteRealTimeDatas" + httpsuffix;//获得最新一期投票结果接口
    public static String videoCountDowun = httpSite + "activity/countdown" + httpsuffix;//查询当前期状态是否可以投票接口
	public static String userVideoList = httpSite + "video/getVideoByUserId" + httpsuffix;//个人页面接口
    public static String copyWriter = httpSite + "user/vote" + httpsuffix;//资源配置接口
    public static String updateUserInfo = httpSite + "user/updateUserInfo" + httpsuffix;
    public static String getVoteCount = httpSite + "user/getTotal2Used" + httpsuffix;
    public static String getShareUrl = httpSite + "video/getpath" + httpsuffix;

}
