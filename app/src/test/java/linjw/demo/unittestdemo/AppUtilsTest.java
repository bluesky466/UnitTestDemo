package linjw.demo.unittestdemo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

/**
 * Created by linjw on 2017/8/26.
 * e-mail : bluesky466@qq.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AppUtils.class)
public class AppUtilsTest {
    @Mock
    private Context mContext;

    @Mock
    private Intent mIntent;

    private AppUtils.StartAppParam mParam;

    @Before
    public void setUp() throws Exception {
        whenNew(Intent.class).withNoArguments().thenReturn(mIntent);
        whenNew(Intent.class).withArguments(anyString()).thenReturn(mIntent);
        mParam = mock(AppUtils.StartAppParam.class);
    }

    @Test
    public void startAppByEmptyArg() {
        assertNotNull(mContext);

        assertFalse(AppUtils.startApp(null, null));
        assertFalse(AppUtils.startApp(mContext, null));
        assertFalse(AppUtils.startApp(null, new AppUtils.StartAppParam()));
    }

    @Test
    public void startAppByPackageAndActivity() {
        given(mParam.getPackageName()).willReturn("packageName");
        given(mParam.getActivity()).willReturn("activity");

        //when
        assertTrue(AppUtils.startApp(mContext, mParam));

        then(mIntent).should().setClassName(mParam.getPackageName(), mParam.getActivity());
        then(mContext).should().startActivity(mIntent);
    }

    @Test
    public void startAppByPackage() {
        mockStatic(AppUtils.class);

        given(AppUtils.startApp(any(Context.class), any(AppUtils.StartAppParam.class)))
                .willCallRealMethod();
        given(AppUtils.getLaunchActivityByPackage(any(Context.class), anyString()))
                .willReturn("LauncActivity");
        given(mParam.getPackageName()).willReturn("packageName");


        //when
        assertTrue(AppUtils.startApp(mContext, mParam));

        //then
        verifyStatic(); //开启static方法的验证,需要开启才能验证AppUtils.getLaunchActivityByPackage是否被调用
        AppUtils.getLaunchActivityByPackage(any(Context.class), eq("packageName"));
        then(mIntent).should().setClassName(mParam.getPackageName(), "LauncActivity");
        then(mContext).should().startActivity(mIntent);
    }

    @Test
    public void startAppByAction() {
        given(mParam.getAction()).willReturn("action");

        //when
        assertTrue(AppUtils.startApp(mContext, mParam));

        then(mContext).should().startActivity(mIntent);
        then(mIntent).should(never()).setData(any(Uri.class));
        then(mIntent).should(never()).addCategory(anyString());
        then(mIntent).should(never()).setClassName(anyString(), anyString());
    }

    @Test
    public void startAppByActionAndCategory() {
        given(mParam.getAction()).willReturn("action");
        given(mParam.getCagtegorys()).willReturn(new ArrayList<String>() {
            {
                add("category1");
                add("category2");
            }
        });

        //when
        assertTrue(AppUtils.startApp(mContext, mParam));

        then(mIntent).should(never()).setClassName(any(Context.class), anyString());
        then(mIntent).should(times(2)).addCategory(anyString());
        then(mIntent).should(times(1)).addCategory("category1");
        then(mIntent).should(times(1)).addCategory("category2");
        then(mContext).should().startActivity(mIntent);
    }

    @Test
    @PrepareForTest({AppUtils.class, Uri.class})
    public void startAppByUri() {
        Uri uri = mock(Uri.class);
        mockStatic(Uri.class);

        given(Uri.parse(anyString())).willReturn(uri);
        given(mParam.getUri()).willReturn("uri");

        //when
        assertTrue(AppUtils.startApp(mContext, mParam));

        //then
        verifyStatic(); //开启static方法的验证,需要开启才能验证Uri.parse是否被调用
        Uri.parse("uri");
        then(mIntent).should().setData(uri);
        then(mIntent).should(never()).addCategory(anyString());
        then(mIntent).should(never()).setAction(anyString());
        then(mIntent).should(never()).setClassName(anyString(), anyString());
        then(mContext).should().startActivity(mIntent);
    }
}