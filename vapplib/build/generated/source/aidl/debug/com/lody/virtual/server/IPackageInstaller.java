/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\com\\lody\\virtual\\server\\IPackageInstaller.aidl
 */
package com.lody.virtual.server;
public interface IPackageInstaller extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lody.virtual.server.IPackageInstaller
{
private static final java.lang.String DESCRIPTOR = "com.lody.virtual.server.IPackageInstaller";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lody.virtual.server.IPackageInstaller interface,
 * generating a proxy if needed.
 */
public static com.lody.virtual.server.IPackageInstaller asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lody.virtual.server.IPackageInstaller))) {
return ((com.lody.virtual.server.IPackageInstaller)iin);
}
return new com.lody.virtual.server.IPackageInstaller.Stub.Proxy(obj);
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
case TRANSACTION_createSession:
{
data.enforceInterface(DESCRIPTOR);
com.lody.virtual.server.pm.installer.SessionParams _arg0;
if ((0!=data.readInt())) {
_arg0 = com.lody.virtual.server.pm.installer.SessionParams.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
int _result = this.createSession(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_updateSessionAppIcon:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.graphics.Bitmap _arg1;
if ((0!=data.readInt())) {
_arg1 = android.graphics.Bitmap.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.updateSessionAppIcon(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_updateSessionAppLabel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.updateSessionAppLabel(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_abandonSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.abandonSession(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_openSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.content.pm.IPackageInstallerSession _result = this.openSession(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getSessionInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.lody.virtual.server.pm.installer.SessionInfo _result = this.getSessionInfo(_arg0);
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
case TRANSACTION_getAllSessions:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.lody.virtual.helper.proto.VParceledListSlice _result = this.getAllSessions(_arg0);
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
case TRANSACTION_getMySessions:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
com.lody.virtual.helper.proto.VParceledListSlice _result = this.getMySessions(_arg0, _arg1);
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
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
android.content.pm.IPackageInstallerCallback _arg0;
_arg0 = android.content.pm.IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
int _arg1;
_arg1 = data.readInt();
this.registerCallback(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
android.content.pm.IPackageInstallerCallback _arg0;
_arg0 = android.content.pm.IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_uninstall:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
android.content.IntentSender _arg3;
if ((0!=data.readInt())) {
_arg3 = android.content.IntentSender.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
int _arg4;
_arg4 = data.readInt();
this.uninstall(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_setPermissionsResult:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.setPermissionsResult(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lody.virtual.server.IPackageInstaller
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
@Override public int createSession(com.lody.virtual.server.pm.installer.SessionParams params, java.lang.String installerPackageName, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((params!=null)) {
_data.writeInt(1);
params.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(installerPackageName);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_createSession, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void updateSessionAppIcon(int sessionId, android.graphics.Bitmap appIcon) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
if ((appIcon!=null)) {
_data.writeInt(1);
appIcon.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateSessionAppIcon, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateSessionAppLabel(int sessionId, java.lang.String appLabel) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
_data.writeString(appLabel);
mRemote.transact(Stub.TRANSACTION_updateSessionAppLabel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void abandonSession(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_abandonSession, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public android.content.pm.IPackageInstallerSession openSession(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.content.pm.IPackageInstallerSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_openSession, _data, _reply, 0);
_reply.readException();
_result = android.content.pm.IPackageInstallerSession.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.lody.virtual.server.pm.installer.SessionInfo getSessionInfo(int sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.server.pm.installer.SessionInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
mRemote.transact(Stub.TRANSACTION_getSessionInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.lody.virtual.server.pm.installer.SessionInfo.CREATOR.createFromParcel(_reply);
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
@Override public com.lody.virtual.helper.proto.VParceledListSlice getAllSessions(int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.VParceledListSlice _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_getAllSessions, _data, _reply, 0);
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
@Override public com.lody.virtual.helper.proto.VParceledListSlice getMySessions(java.lang.String installerPackageName, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.lody.virtual.helper.proto.VParceledListSlice _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(installerPackageName);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_getMySessions, _data, _reply, 0);
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
@Override public void registerCallback(android.content.pm.IPackageInstallerCallback callback, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterCallback(android.content.pm.IPackageInstallerCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void uninstall(java.lang.String packageName, java.lang.String callerPackageName, int flags, android.content.IntentSender statusReceiver, int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(callerPackageName);
_data.writeInt(flags);
if ((statusReceiver!=null)) {
_data.writeInt(1);
statusReceiver.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_uninstall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPermissionsResult(int sessionId, boolean accepted) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sessionId);
_data.writeInt(((accepted)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setPermissionsResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_createSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_updateSessionAppIcon = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateSessionAppLabel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_abandonSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_openSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getSessionInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getAllSessions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getMySessions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_uninstall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_setPermissionsResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public int createSession(com.lody.virtual.server.pm.installer.SessionParams params, java.lang.String installerPackageName, int userId) throws android.os.RemoteException;
public void updateSessionAppIcon(int sessionId, android.graphics.Bitmap appIcon) throws android.os.RemoteException;
public void updateSessionAppLabel(int sessionId, java.lang.String appLabel) throws android.os.RemoteException;
public void abandonSession(int sessionId) throws android.os.RemoteException;
public android.content.pm.IPackageInstallerSession openSession(int sessionId) throws android.os.RemoteException;
public com.lody.virtual.server.pm.installer.SessionInfo getSessionInfo(int sessionId) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.VParceledListSlice getAllSessions(int userId) throws android.os.RemoteException;
public com.lody.virtual.helper.proto.VParceledListSlice getMySessions(java.lang.String installerPackageName, int userId) throws android.os.RemoteException;
public void registerCallback(android.content.pm.IPackageInstallerCallback callback, int userId) throws android.os.RemoteException;
public void unregisterCallback(android.content.pm.IPackageInstallerCallback callback) throws android.os.RemoteException;
public void uninstall(java.lang.String packageName, java.lang.String callerPackageName, int flags, android.content.IntentSender statusReceiver, int userId) throws android.os.RemoteException;
public void setPermissionsResult(int sessionId, boolean accepted) throws android.os.RemoteException;
}
