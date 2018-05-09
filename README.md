
# 一段简单的代码 android 6.0动态权限适配工具
用不习惯其他第三方的权限适配工具，空余时间，自己动手封装了一个简洁的权限适配工具
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
	        implementation 'com.github.Xiaohy61:MyPermission:1.1'
	}

```

### 使用
1.单个权限申请：

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
                }, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA);
```
### RequestPermission ：
1.首先判断是否拥有申请的权限，作状态回调
```xml
public class RequestPermission {


    public static void request(Context context, OnPermissionListener listener, String... permissions) {

	 //如果拥有权限，回调成功结果，缺少或者没有就去PermissionDialogFragment申请权限	
        if(hasPermission(context,permissions)){
            listener.onPermissionSuccess();
        }else {
	 
            PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(permissions);
            dialogFragment.onPermissionListener(listener);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
        }





    }

    /**
     * 是否拥有了申请的权限
     * @param context
     * @param permissions
     * @return boolean
     */
    public static  boolean hasPermission(Context context,String... permissions){
        for (String permission: permissions) {
            if(PermissionChecker.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}
```
### PermissionDialogFragment ：
源码有详细说明，就不罗嗦了

