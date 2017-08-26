package linjw.demo.unittestdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjw on 2017/8/26.
 * e-mail : bluesky466@qq.com
 */

@SuppressWarnings("WeakerAccess")
public class AppUtils {

    /**
     * 启动应用
     *
     * @param context context
     * @param param   启动参数
     * @return 是否启动成功
     */
    public static boolean startApp(Context context, StartAppParam param) {
        if (context == null || param == null) {
            return false;
        }

        if (param.getPackageName() != null && !param.getPackageName().isEmpty()) {
            String activity = param.getActivity();
            if (activity == null || activity.isEmpty()) {
                activity = getLaunchActivityByPackage(context, param.getPackageName());
            }

            if (activity != null && !activity.isEmpty()) {
                Intent intent = new Intent();
                intent.setClassName(param.getPackageName(), activity);
                context.startActivity(intent);
                return true;
            }
        }

        if (param.getAction() != null && !param.getAction().isEmpty()) {
            Intent intent = new Intent(param.getAction());
            for (String category : param.getCagtegorys()) {
                intent.addCategory(category);
            }
            context.startActivity(intent);
            return true;
        }

        if (param.getUri() != null && !param.getUri().isEmpty()) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(param.getUri()));
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static String getLaunchActivityByPackage(Context context, String packageName) {
        //这个方法先不实现,直接返回null,测试的时候直接mock就好了
        return null;
    }

    public static class StartAppParam {
        private String mPackage;
        private String mActivity;
        private String mAction;
        private String mUri;
        private List<String> categorys = new ArrayList<>();

        public String getPackageName() {
            return mPackage;
        }

        public void setPackageName(String packageName) {
            this.mPackage = packageName;
        }

        public String getActivity() {
            return mActivity;
        }

        public void setActivity(String activity) {
            this.mActivity = activity;
        }

        public String getAction() {
            return mAction;
        }

        public void setAction(String action) {
            this.mAction = action;
        }

        public void addCategory(String category) {
            categorys.add(category);
        }

        public List<String> getCagtegorys() {
            return categorys;
        }

        public String getUri() {
            return mUri;
        }

        public void setUri(String uri) {
            this.mUri = uri;
        }
    }
}
