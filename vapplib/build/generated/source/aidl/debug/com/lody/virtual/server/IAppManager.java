/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\com\\lody\\virtual\\server\\IAppManager.aidl
 */
package com.lody.virtual.server;
public interface IAppManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lody.virtual.server.IAppManager
{
private static final java.lang.String DESCRIPTOR = "com.lody.virtual.server.IAppManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lody.virtual.server.IAppManager interface,
 * generating a proxy if needed.
 */
public static com.lody.virtual.server.IAppManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lody.virtual.server.IAppManager))) {
return ((com.lody.virtual.server.IAppManager)iin);
}
return new com.lody.virtual.server.IAppManager.Stub.Proxy(obj);
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
case TRANSACTION_preloadAllApps:
{
data.enforceInterface(DESCRIPTOR);
this.preloadAllApps();
reply.writeNoException();
return true;
}
case TRANSACTION_findAppInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.lody.virtual.helper.proto.AppSetting _result = this.findAppInfo(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_installApp:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
com.lody.virtual.helper.proto.InstallResult _result = this.installApp(_arg0, _arg1);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_uninstallApp:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.uninstallApp(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getAllApps:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.lody.virtual.helper.proto.AppSetting> _result = this.getAllApps();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getAppCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAppCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isAppInstalled:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isAppInstalled(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IAppObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IAppObserver.Stub.asInterface(data.readStrongBinder());
this.registerObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IAppObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IAppObserver.Stub.asInterface(data.readStrongBinder());
this.unregisterObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setAppRequestListener:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IAppRequestListener _arg0;
_arg0 = com.lody.virtual.server.interfaces.IAppRequestListener.Stub.asInterface(data.readStrongBinder());
this.setAppRequestListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_clearAppRequestListener:
{
data.enforceInterface(DESCRIPTOR);
this.clearAppRequestListener();
reply.writeNoException();
return true;
}
case TRANSACTION_getAppRequestListener:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IAppRequestListener _result = this.getAppRequestListener();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lody.virtual.server.IAppManager
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
@Override public void preloadAllApps() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_preloadAllApps, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.lody.virtual.helper.proto.AppSetting findAppInfo(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.AppSetting _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_findAppInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.helper.proto.AppSetting.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.lody.virtual.helper.proto.InstallResult installApp(java.lang.String apkPath, int flags) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.InstallResult _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(apkPath);
_data.writeInt(flags);
mRemote.transact(Stub.TRANSACTION_installApp, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.helper.proto.InstallResult.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean uninstallApp(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_uninstallApp, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<com.lody.virtual.helper.proto.AppSetting> getAllApps() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.lody.virtual.helper.proto.AppSetting> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAllApps, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.lody.virtual.helper.proto.AppSetting.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAppCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAppCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAppInstalled(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_isAppInstalled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerObserver(com.lody.virtual.server.interfaces.IAppObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterObserver(com.lody.virtual.server.interfaces.IAppObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setAppRequestListener(com.lody.virtual.server.interfaces.IAppRequestListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setAppRequestListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void clearAppRequestListener() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearAppRequestListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.lody.virtual.server.interfaces.IAppRequestListener getAppRequestListener() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.server.interfaces.IAppRequestListener _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAppRequestListener, _data, _reply, 0);
_reply.readException();
_result = com.lody.virtual.server.interfaces.IAppRequestListener.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_preloadAllApps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_findAppInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_installApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_uninstallApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getAllApps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getAppCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isAppInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_registerObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_unregisterObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setAppRequestListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_clearAppRequestListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getAppRequestListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public void preloadAllApps() throws android.os.RemoteException;
public com.lody.virtual.helper.proto.AppSetting findAppInfo(java.lang.String pkg) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.InstallResult installApp(java.lang.String apkPath, int flags) throws android.os.RemoteException;
public boolean uninstallApp(java.lang.String pkg) throws android.os.RemoteException;
public java.util.List<com.lody.virtual.helper.proto.AppSetting> getAllApps() throws android.os.RemoteException;
public int getAppCount() throws android.os.RemoteException;
public boolean isAppInstalled(java.lang.String pkg) throws android.os.RemoteException;
public void registerObserver(com.lody.virtual.server.interfaces.IAppObserver observer) throws android.os.RemoteException;
public void unregisterObserver(com.lody.virtual.server.interfaces.IAppObserver observer) throws android.os.RemoteException;
public void setAppRequestListener(com.lody.virtual.server.interfaces.IAppRequestListener listener) throws android.os.RemoteException;
public void clearAppRequestListener() throws android.os.RemoteException;
public com.lody.virtual.server.interfaces.IAppRequestListener getAppRequestListener() throws android.os.RemoteException;
}
