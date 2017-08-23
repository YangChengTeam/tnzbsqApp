/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\com\\lody\\virtual\\client\\IVClient.aidl
 */
package com.lody.virtual.client;
public interface IVClient extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lody.virtual.client.IVClient
{
private static final java.lang.String DESCRIPTOR = "com.lody.virtual.client.IVClient";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lody.virtual.client.IVClient interface,
 * generating a proxy if needed.
 */
public static com.lody.virtual.client.IVClient asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lody.virtual.client.IVClient))) {
return ((com.lody.virtual.client.IVClient)iin);
}
return new com.lody.virtual.client.IVClient.Stub.Proxy(obj);
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
case TRANSACTION_scheduleReceiver:
{
data.enforceInterface(DESCRIPTOR);
android.content.ComponentName _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.content.Intent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
com.lody.virtual.helper.proto.PendingResultData _arg2;
if ((0!=data.readInt())) {
_arg2 = com.lody.virtual.helper.proto.PendingResultData.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.scheduleReceiver(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_scheduleNewIntent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.IBinder _arg1;
_arg1 = data.readStrongBinder();
android.content.Intent _arg2;
if ((0!=data.readInt())) {
_arg2 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.scheduleNewIntent(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_finishActivity:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
this.finishActivity(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_createProxyService:
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
android.os.IBinder _result = this.createProxyService(_arg0, _arg1);
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_acquireProviderClient:
{
data.enforceInterface(DESCRIPTOR);
android.content.pm.ProviderInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.pm.ProviderInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
android.os.IBinder _result = this.acquireProviderClient(_arg0);
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_getAppThread:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _result = this.getAppThread();
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_getToken:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _result = this.getToken();
reply.writeNoException();
reply.writeStrongBinder(_result);
return true;
}
case TRANSACTION_getDebugInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getDebugInfo();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lody.virtual.client.IVClient
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
@Override public void scheduleReceiver(android.content.ComponentName component, android.content.Intent intent, com.lody.virtual.helper.proto.PendingResultData resultData) throws android.os.RemoteException
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
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((resultData!=null)) {
_data.writeInt(1);
resultData.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_scheduleReceiver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void scheduleNewIntent(java.lang.String creator, android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(creator);
_data.writeStrongBinder(token);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_scheduleNewIntent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void finishActivity(android.os.IBinder token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
mRemote.transact(Stub.TRANSACTION_finishActivity, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public android.os.IBinder createProxyService(android.content.ComponentName component, android.os.IBinder binder) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((component!=null)) {
_data.writeInt(1);
component.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder(binder);
mRemote.transact(Stub.TRANSACTION_createProxyService, _data, _reply, 0);
_reply.readException();
_result = _reply.readStrongBinder();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.os.IBinder acquireProviderClient(android.content.pm.ProviderInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
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
@Override public android.os.IBinder getAppThread() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAppThread, _data, _reply, 0);
_reply.readException();
_result = _reply.readStrongBinder();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.os.IBinder getToken() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.IBinder _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getToken, _data, _reply, 0);
_reply.readException();
_result = _reply.readStrongBinder();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getDebugInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDebugInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_scheduleReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_scheduleNewIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_finishActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_createProxyService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_acquireProviderClient = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getAppThread = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getDebugInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
public void scheduleReceiver(android.content.ComponentName component, android.content.Intent intent, com.lody.virtual.helper.proto.PendingResultData resultData) throws android.os.RemoteException;
public void scheduleNewIntent(java.lang.String creator, android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException;
public void finishActivity(android.os.IBinder token) throws android.os.RemoteException;
public android.os.IBinder createProxyService(android.content.ComponentName component, android.os.IBinder binder) throws android.os.RemoteException;
public android.os.IBinder acquireProviderClient(android.content.pm.ProviderInfo info) throws android.os.RemoteException;
public android.os.IBinder getAppThread() throws android.os.RemoteException;
public android.os.IBinder getToken() throws android.os.RemoteException;
public java.lang.String getDebugInfo() throws android.os.RemoteException;
}
