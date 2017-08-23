/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\android\\content\\pm\\IPackageInstallObserver2.aidl
 */
package android.content.pm;
/**
 * API for installation callbacks from the Package Manager.  In certain result cases
 * additional information will be provided.
 */
public interface IPackageInstallObserver2 extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.content.pm.IPackageInstallObserver2
{
private static final java.lang.String DESCRIPTOR = "android.content.pm.IPackageInstallObserver2";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.content.pm.IPackageInstallObserver2 interface,
 * generating a proxy if needed.
 */
public static android.content.pm.IPackageInstallObserver2 asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.content.pm.IPackageInstallObserver2))) {
return ((android.content.pm.IPackageInstallObserver2)iin);
}
return new android.content.pm.IPackageInstallObserver2.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onUserActionRequired:
{
data.enforceInterface(DESCRIPTOR);
android.content.Intent _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onUserActionRequired(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPackageInstalled:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
this.onPackageInstalled(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.content.pm.IPackageInstallObserver2
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onUserActionRequired, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * The install operation has completed.  {@code returnCode} holds a numeric code
     * indicating success or failure.  In certain cases the {@code extras} Bundle will
     * contain additional details:
     *
     * <p><table>
     * <tr>
     *   <td>INSTALL_FAILED_DUPLICATE_PERMISSION</td>
     *   <td>Two strings are provided in the extras bundle: EXTRA_EXISTING_PERMISSION
     *       is the name of the permission that the app is attempting to define, and
     *       EXTRA_EXISTING_PACKAGE is the package name of the app which has already
     *       defined the permission.</td>
     * </tr>
     * </table>
     */
@Override public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(basePackageName);
_data.writeInt(returnCode);
_data.writeString(msg);
if ((extras!=null)) {
_data.writeInt(1);
extras.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onPackageInstalled, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onUserActionRequired = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onPackageInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException;
/**
     * The install operation has completed.  {@code returnCode} holds a numeric code
     * indicating success or failure.  In certain cases the {@code extras} Bundle will
     * contain additional details:
     *
     * <p><table>
     * <tr>
     *   <td>INSTALL_FAILED_DUPLICATE_PERMISSION</td>
     *   <td>Two strings are provided in the extras bundle: EXTRA_EXISTING_PERMISSION
     *       is the name of the permission that the app is attempting to define, and
     *       EXTRA_EXISTING_PACKAGE is the package name of the app which has already
     *       defined the permission.</td>
     * </tr>
     * </table>
     */
public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) throws android.os.RemoteException;
}
