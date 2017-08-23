/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\com\\lody\\virtual\\server\\IActivityManager.aidl
 */
package com.lody.virtual.server;
public interface IActivityManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lody.virtual.server.IActivityManager
{
private static final java.lang.String DESCRIPTOR = "com.lody.virtual.server.IActivityManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lody.virtual.server.IActivityManager interface,
 * generating a proxy if needed.
 */
public static com.lody.virtual.server.IActivityManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lody.virtual.server.IActivityManager))) {
return ((com.lody.virtual.server.IActivityManager)iin);
}
return new com.lody.virtual.server.IActivityManager.Stub.Proxy(obj);
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
case TRANSACTION_initProcess:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
int _result = this.initProcess(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getFreeStubCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getFreeStubCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getSystemPid:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSystemPid();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getUidByPid:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getUidByPid(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_registerUIObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IUiObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IUiObserver.Stub.asInterface(data.readStrongBinder());
this.registerUIObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterUIObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IUiObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IUiObserver.Stub.asInterface(data.readStrongBinder());
this.unregisterUIObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isAppProcess:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isAppProcess(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAppRunning:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
boolean _result = this.isAppRunning(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAppPid:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.isAppPid(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getAppProcessName:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _result = this.getAppProcessName(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getProcessPkgList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.util.List<java.lang.String> _result = this.getProcessPkgList(_arg0);
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_killAllApps:
{
data.enforceInterface(DESCRIPTOR);
this.killAllApps();
reply.writeNoException();
return true;
}
case TRANSACTION_killAppByPkg:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.killAppByPkg(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_killApplicationProcess:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.killApplicationProcess(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_dump:
{
data.enforceInterface(DESCRIPTOR);
this.dump();
reply.writeNoException();
return true;
}
case TRANSACTION_registerProcessObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IProcessObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IProcessObserver.Stub.asInterface(data.readStrongBinder());
this.registerProcessObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterProcessObserver:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.interfaces.IProcessObserver _arg0;
_arg0 = com.lody.virtual.server.interfaces.IProcessObserver.Stub.asInterface(data.readStrongBinder());
this.unregisterProcessObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getInitialPackage:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _result = this.getInitialPackage(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_handleApplicationCrash:
{
data.enforceInterface(DESCRIPTOR);
this.handleApplicationCrash();
reply.writeNoException();
return true;
}
case TRANSACTION_appDoneExecuting:
{
data.enforceInterface(DESCRIPTOR);
this.appDoneExecuting();
reply.writeNoException();
return true;
}
case TRANSACTION_startActivity:
{
data.enforceInterface(DESCRIPTOR);
android.content.Intent _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.content.pm.ActivityInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.pm.ActivityInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
android.os.IBinder _arg2;
_arg2 = data.readStrongBinder();
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
java.lang.String _arg4;
_arg4 = data.readString();
int _arg5;
_arg5 = data.readInt();
int _arg6;
_arg6 = data.readInt();
int _result = this.startActivity(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onActivityCreated:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.content.ComponentName _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
android.os.IBinder _arg2;
_arg2 = data.readStrongBinder();
android.content.Intent _arg3;
if ((0!=data.readInt())) {
_arg3 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
java.lang.String _arg4;
_arg4 = data.readString();
int _arg5;
_arg5 = data.readInt();
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
this.onActivityCreated(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
reply.writeNoException();
return true;
}
case TRANSACTION_onActivityResumed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
this.onActivityResumed(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onActivityDestroyed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
boolean _result = this.onActivityDestroyed(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getActivityClassForToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
android.content.ComponentName _result = this.getActivityClassForToken(_arg0, _arg1);
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
case TRANSACTION_getCallingPackage:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
java.lang.String _result = this.getCallingPackage(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCallingActivity:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
android.content.ComponentName _result = this.getCallingActivity(_arg0, _arg1);
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
case TRANSACTION_getTaskInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.lody.virtual.helper.proto.AppTaskInfo _result = this.getTaskInfo(_arg0);
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
case TRANSACTION_getPackageForToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
java.lang.String _result = this.getPackageForToken(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isVAServiceToken:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
boolean _result = this.isVAServiceToken(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_startService:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.content.Intent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
android.content.ComponentName _result = this.startService(_arg0, _arg1, _arg2, _arg3);
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
case TRANSACTION_stopService:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.content.Intent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _result = this.stopService(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_stopServiceToken:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
boolean _result = this.stopServiceToken(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setServiceForeground:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
int _arg2;
_arg2 = data.readInt();
android.app.Notification _arg3;
if ((0!=data.readInt())) {
_arg3 = android.app.Notification.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
boolean _arg4;
_arg4 = (0!=data.readInt());
int _arg5;
_arg5 = data.readInt();
this.setServiceForeground(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_bindService:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
android.content.Intent _arg2;
if ((0!=data.readInt())) {
_arg2 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String _arg3;
_arg3 = data.readString();
android.app.IServiceConnection _arg4;
_arg4 = android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder());
int _arg5;
_arg5 = data.readInt();
int _arg6;
_arg6 = data.readInt();
int _result = this.bindService(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_unbindService:
{
data.enforceInterface(DESCRIPTOR);
android.app.IServiceConnection _arg0;
_arg0 = android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder());
int _arg1;
_arg1 = data.readInt();
boolean _result = this.unbindService(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unbindFinished:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.content.Intent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
boolean _arg2;
_arg2 = (0!=data.readInt());
int _arg3;
_arg3 = data.readInt();
this.unbindFinished(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_serviceDoneExecuting:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
this.serviceDoneExecuting(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_peekService:
{
data.enforceInterface(DESCRIPTOR);
android.content.Intent _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
android.os.IBinder _result = this.peekService(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_publishService:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.content.Intent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
android.os.IBinder _arg2;
_arg2 = data.readStrongBinder();
int _arg3;
_arg3 = data.readInt();
this.publishService(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_getServices:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
com.lody.virtual.helper.proto.VParceledListSlice _result = this.getServices(_arg0, _arg1, _arg2);
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
case TRANSACTION_acquireProviderClient:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.content.pm.ProviderInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.pm.ProviderInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
android.os.IBinder _result = this.acquireProviderClient(_arg0, _arg1);
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_getPendingIntent:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
com.lody.virtual.helper.proto.PendingIntentData _result = this.getPendingIntent(_arg0);
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
case TRANSACTION_addPendingIntent:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
java.lang.String _arg1;
_arg1 = data.readString();
this.addPendingIntent(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removePendingIntent:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
this.removePendingIntent(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_processRestarted:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.processRestarted(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_broadcastFinish:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.helper.proto.PendingResultData _arg0;
if ((0!=data.readInt())) {
_arg0 = com.lody.virtual.helper.proto.PendingResultData.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.broadcastFinish(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_dispatchStickyBroadcast:
{
data.enforceInterface(DESCRIPTOR);
android.content.IntentFilter _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.IntentFilter.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.content.Intent _result = this.dispatchStickyBroadcast(_arg0);
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
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lody.virtual.server.IActivityManager
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
@Override public int initProcess(java.lang.String packageName, java.lang.String processName, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(processName);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_initProcess, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getFreeStubCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFreeStubCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getSystemPid() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSystemPid, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getUidByPid(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_getUidByPid, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerUIObserver(com.lody.virtual.server.interfaces.IUiObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerUIObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterUIObserver(com.lody.virtual.server.interfaces.IUiObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterUIObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isAppProcess(java.lang.String processName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(processName);
mRemote.transact(Stub.TRANSACTION_isAppProcess, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAppRunning(java.lang.String packageName, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_isAppRunning, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAppPid(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_isAppPid, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getAppProcessName(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_getAppProcessName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getProcessPkgList(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_getProcessPkgList, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void killAllApps() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_killAllApps, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void killAppByPkg(java.lang.String pkg, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_killAppByPkg, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void killApplicationProcess(java.lang.String procName, int vuid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(procName);
_data.writeInt(vuid);
mRemote.transact(Stub.TRANSACTION_killApplicationProcess, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void dump() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registerProcessObserver(com.lody.virtual.server.interfaces.IProcessObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerProcessObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterProcessObserver(com.lody.virtual.server.interfaces.IProcessObserver observer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((observer!=null))?(observer.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterProcessObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getInitialPackage(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_getInitialPackage, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void handleApplicationCrash() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_handleApplicationCrash, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void appDoneExecuting() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_appDoneExecuting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int startActivity(android.content.Intent intent, android.content.pm.ActivityInfo info, android.os.IBinder resultTo, android.os.Bundle options, java.lang.String resultWho, int requestCode, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(resultTo);
if ((options!=null)) {
_data.writeInt(1);
options.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(resultWho);
_data.writeInt(requestCode);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_startActivity, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void onActivityCreated(android.content.ComponentName component, android.content.ComponentName caller, android.os.IBinder token, android.content.Intent intent, java.lang.String affinity, int taskId, int launchMode, int flags) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((component!=null)) {
_data.writeInt(1);
component.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((caller!=null)) {
_data.writeInt(1);
caller.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(token);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(affinity);
_data.writeInt(taskId);
_data.writeInt(launchMode);
_data.writeInt(flags);
mRemote.transact(Stub.TRANSACTION_onActivityCreated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onActivityResumed(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_onActivityResumed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean onActivityDestroyed(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_onActivityDestroyed, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.content.ComponentName getActivityClassForToken(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.content.ComponentName _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_getActivityClassForToken, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.content.ComponentName.CREATOR.createFromParcel(_reply);
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
@Override public java.lang.String getCallingPackage(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_getCallingPackage, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.content.ComponentName getCallingActivity(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.content.ComponentName _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_getCallingActivity, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.content.ComponentName.CREATOR.createFromParcel(_reply);
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
@Override public com.lody.virtual.helper.proto.AppTaskInfo getTaskInfo(int taskId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.AppTaskInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(taskId);
mRemote.transact(Stub.TRANSACTION_getTaskInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.helper.proto.AppTaskInfo.CREATOR.createFromParcel(_reply);
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
@Override public java.lang.String getPackageForToken(int userId, android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_getPackageForToken, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isVAServiceToken(android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_isVAServiceToken, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.content.ComponentName startService(android.os.IBinder caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.content.ComponentName _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(caller);
if ((service!=null)) {
_data.writeInt(1);
service.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(resolvedType);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_startService, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.content.ComponentName.CREATOR.createFromParcel(_reply);
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
@Override public int stopService(android.os.IBinder caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(caller);
if ((service!=null)) {
_data.writeInt(1);
service.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(resolvedType);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean stopServiceToken(android.content.ComponentName className, android.os.IBinder token, int startId, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((className!=null)) {
_data.writeInt(1);
className.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(token);
_data.writeInt(startId);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_stopServiceToken, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setServiceForeground(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, boolean keepNotification, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((className!=null)) {
_data.writeInt(1);
className.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(token);
_data.writeInt(id);
if ((notification!=null)) {
_data.writeInt(1);
notification.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((keepNotification)?(1):(0)));
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_setServiceForeground, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int bindService(android.os.IBinder caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, int flags, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(caller);
_data.writeStrongBinder(token);
if ((service!=null)) {
_data.writeInt(1);
service.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(resolvedType);
_data.writeStrongBinder((((connection!=null))?(connection.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_bindService, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unbindService(android.app.IServiceConnection connection, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((connection!=null))?(connection.asBinder()):(null)));
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_unbindService, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void unbindFinished(android.os.IBinder token, android.content.Intent service, boolean doRebind, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
if ((service!=null)) {
_data.writeInt(1);
service.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((doRebind)?(1):(0)));
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_unbindFinished, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void serviceDoneExecuting(android.os.IBinder token, int type, int startId, int res, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
_data.writeInt(type);
_data.writeInt(startId);
_data.writeInt(res);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_serviceDoneExecuting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public android.os.IBinder peekService(android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((service!=null)) {
_data.writeInt(1);
service.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(resolvedType);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_peekService, _data, _reply, 0);
_reply.readException();
_result = _reply.readStrongBinder();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void publishService(android.os.IBinder token, android.content.Intent intent, android.os.IBinder service, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(service);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_publishService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.lody.virtual.helper.proto.VParceledListSlice getServices(int maxNum, int flags, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.VParceledListSlice _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(maxNum);
_data.writeInt(flags);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_getServices, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.helper.proto.VParceledListSlice.CREATOR.createFromParcel(_reply);
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
@Override public android.os.IBinder acquireProviderClient(int userId, android.content.pm.ProviderInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_acquireProviderClient, _data, _reply, 0);
_reply.readException();
_result = _reply.readStrongBinder();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.lody.virtual.helper.proto.PendingIntentData getPendingIntent(android.os.IBinder binder) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.PendingIntentData _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(binder);
mRemote.transact(Stub.TRANSACTION_getPendingIntent, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.helper.proto.PendingIntentData.CREATOR.createFromParcel(_reply);
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
@Override public void addPendingIntent(android.os.IBinder binder, java.lang.String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(binder);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_addPendingIntent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removePendingIntent(android.os.IBinder binder) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(binder);
mRemote.transact(Stub.TRANSACTION_removePendingIntent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void processRestarted(java.lang.String packageName, java.lang.String processName, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(processName);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_processRestarted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void broadcastFinish(com.lody.virtual.helper.proto.PendingResultData res) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((res!=null)) {
_data.writeInt(1);
res.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_broadcastFinish, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public android.content.Intent dispatchStickyBroadcast(android.content.IntentFilter filter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.content.Intent _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((filter!=null)) {
_data.writeInt(1);
filter.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_dispatchStickyBroadcast, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.content.Intent.CREATOR.createFromParcel(_reply);
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
}
static final int TRANSACTION_initProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getFreeStubCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getSystemPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getUidByPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_registerUIObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_unregisterUIObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isAppProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_isAppRunning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_isAppPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getAppProcessName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getProcessPkgList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_killAllApps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_killAppByPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_killApplicationProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_registerProcessObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_unregisterProcessObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getInitialPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_handleApplicationCrash = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_appDoneExecuting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_startActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_onActivityCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_onActivityResumed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_onActivityDestroyed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_getActivityClassForToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_getCallingPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_getCallingActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_getTaskInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_getPackageForToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_isVAServiceToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_startService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_stopServiceToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_setServiceForeground = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_bindService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_unbindService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_unbindFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_serviceDoneExecuting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_peekService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_publishService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_getServices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_acquireProviderClient = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_getPendingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_addPendingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_removePendingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_processRestarted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_broadcastFinish = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_dispatchStickyBroadcast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
}
public int initProcess(java.lang.String packageName, java.lang.String processName, int userId) throws android.os.RemoteException;
public int getFreeStubCount() throws android.os.RemoteException;
public int getSystemPid() throws android.os.RemoteException;
public int getUidByPid(int pid) throws android.os.RemoteException;
public void registerUIObserver(com.lody.virtual.server.interfaces.IUiObserver observer) throws android.os.RemoteException;
public void unregisterUIObserver(com.lody.virtual.server.interfaces.IUiObserver observer) throws android.os.RemoteException;
public boolean isAppProcess(java.lang.String processName) throws android.os.RemoteException;
public boolean isAppRunning(java.lang.String packageName, int userId) throws android.os.RemoteException;
public boolean isAppPid(int pid) throws android.os.RemoteException;
public java.lang.String getAppProcessName(int pid) throws android.os.RemoteException;
public java.util.List<java.lang.String> getProcessPkgList(int pid) throws android.os.RemoteException;
public void killAllApps() throws android.os.RemoteException;
public void killAppByPkg(java.lang.String pkg, int userId) throws android.os.RemoteException;
public void killApplicationProcess(java.lang.String procName, int vuid) throws android.os.RemoteException;
public void dump() throws android.os.RemoteException;
public void registerProcessObserver(com.lody.virtual.server.interfaces.IProcessObserver observer) throws android.os.RemoteException;
public void unregisterProcessObserver(com.lody.virtual.server.interfaces.IProcessObserver observer) throws android.os.RemoteException;
public java.lang.String getInitialPackage(int pid) throws android.os.RemoteException;
public void handleApplicationCrash() throws android.os.RemoteException;
public void appDoneExecuting() throws android.os.RemoteException;
public int startActivity(android.content.Intent intent, android.content.pm.ActivityInfo info, android.os.IBinder resultTo, android.os.Bundle options, java.lang.String resultWho, int requestCode, int userId) throws android.os.RemoteException;
public void onActivityCreated(android.content.ComponentName component, android.content.ComponentName caller, android.os.IBinder token, android.content.Intent intent, java.lang.String affinity, int taskId, int launchMode, int flags) throws android.os.RemoteException;
public void onActivityResumed(int userId, android.os.IBinder token) throws android.os.RemoteException;
public boolean onActivityDestroyed(int userId, android.os.IBinder token) throws android.os.RemoteException;
public android.content.ComponentName getActivityClassForToken(int userId, android.os.IBinder token) throws android.os.RemoteException;
public java.lang.String getCallingPackage(int userId, android.os.IBinder token) throws android.os.RemoteException;
public android.content.ComponentName getCallingActivity(int userId, android.os.IBinder token) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.AppTaskInfo getTaskInfo(int taskId) throws android.os.RemoteException;
public java.lang.String getPackageForToken(int userId, android.os.IBinder token) throws android.os.RemoteException;
public boolean isVAServiceToken(android.os.IBinder token) throws android.os.RemoteException;
public android.content.ComponentName startService(android.os.IBinder caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
public int stopService(android.os.IBinder caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
public boolean stopServiceToken(android.content.ComponentName className, android.os.IBinder token, int startId, int userId) throws android.os.RemoteException;
public void setServiceForeground(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, boolean keepNotification, int userId) throws android.os.RemoteException;
public int bindService(android.os.IBinder caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, int flags, int userId) throws android.os.RemoteException;
public boolean unbindService(android.app.IServiceConnection connection, int userId) throws android.os.RemoteException;
public void unbindFinished(android.os.IBinder token, android.content.Intent service, boolean doRebind, int userId) throws android.os.RemoteException;
public void serviceDoneExecuting(android.os.IBinder token, int type, int startId, int res, int userId) throws android.os.RemoteException;
public android.os.IBinder peekService(android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
public void publishService(android.os.IBinder token, android.content.Intent intent, android.os.IBinder service, int userId) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.VParceledListSlice getServices(int maxNum, int flags, int userId) throws android.os.RemoteException;
public android.os.IBinder acquireProviderClient(int userId, android.content.pm.ProviderInfo info) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.PendingIntentData getPendingIntent(android.os.IBinder binder) throws android.os.RemoteException;
public void addPendingIntent(android.os.IBinder binder, java.lang.String packageName) throws android.os.RemoteException;
public void removePendingIntent(android.os.IBinder binder) throws android.os.RemoteException;
public void processRestarted(java.lang.String packageName, java.lang.String processName, int userId) throws android.os.RemoteException;
public void broadcastFinish(com.lody.virtual.helper.proto.PendingResultData res) throws android.os.RemoteException;
public android.content.Intent dispatchStickyBroadcast(android.content.IntentFilter filter) throws android.os.RemoteException;
}
