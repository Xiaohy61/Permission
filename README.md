[![](https://jitpack.io/v/Xiaohy61/Permission.svg)](https://jitpack.io/#Xiaohy61/Permission)
# 一个优雅的 android 6.0动态权限适配工具

效果图


<img width="40%" height="40%" src="https://img-blog.csdn.net/20180508153227302?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3hoeTYx/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70"/>



### 依赖：

``` xml
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
``` xml
 dependencies {
	        implementation 'com.github.Xiaohy61:Permission:1.0.7'
	}

```

### 使用
1.单个权限申请：

```xml
 RequestPermission.request(MainActivity.this, new OnPermissionListener() {
                    @Override
                    public void onPermissionSuccess() {
                        Toast.makeText(MainActivity.this, "取得权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionFailure(String[] permission) {
                        Toast.makeText(MainActivity.this, "禁止权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelClick() {
                        Toast.makeText(MainActivity.this, "点击取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
```
2.多个权限申请：
```xml
   RequestPermission.request(MainActivity.this, new OnPermissionListener() {
                    @Override
                    public void onPermissionSuccess() {
                        Toast.makeText(MainActivity.this,"取得权限",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionFailure(String[] permission) {
                        Toast.makeText(MainActivity.this,"禁止权限",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelClick() {
                        Toast.makeText(MainActivity.this, "点击取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA);
```
### RequestPermission ：
1.首先判断是否拥有申请的权限，作状态回调
```xml
public class RequestPermission {


    public static void request(@NonNull Context context,@NonNull OnPermissionListener listener,@NonNull String... permissions) {


        if (hasPermission(context, permissions)) {
            listener.onPermissionSuccess();

        } else {

            /**
             * 未取得权限的数量统计
             */
            int count = 0;

            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    count++;
                }
            }
            /**
             * 把未取得权限的重新装箱去请求，目的：以防已请求过的权限多次请求
             */
            String[] unGetPermissions = new String[count];
            int index = 0;

            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    unGetPermissions[index] = permission;
                    index++;
                }
            }


            PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(unGetPermissions);
            dialogFragment.onPermissionListener(listener);
            dialogFragment.setActivityContext(context);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");


        }

    }

    /**
     * 是否拥有了申请的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
```
### PermissionDialogFragment ：
源码有详细说明，就不罗嗦了

