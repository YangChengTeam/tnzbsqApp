/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\zdh\\as_workspace\\Tnzbsq2\\vapplib\\src\\main\\aidl\\com\\lody\\virtual\\server\\IAccountManager.aidl
 */
package com.lody.virtual.server;
/**
 * Central application service that provides account management.
 * @hide
 */
public interface IAccountManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lody.virtual.server.IAccountManager
{
private static final java.lang.String DESCRIPTOR = "com.lody.virtual.server.IAccountManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lody.virtual.server.IAccountManager interface,
 * generating a proxy if needed.
 */
public static com.lody.virtual.server.IAccountManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lody.virtual.server.IAccountManager))) {
return ((com.lody.virtual.server.IAccountManager)iin);
}
return new com.lody.virtual.server.IAccountManager.Stub.Proxy(obj);
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
case TRANSACTION_getAuthenticatorTypes:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.AuthenticatorDescription[] _result = this.getAuthenticatorTypes(_arg0);
reply.writeNoException();
reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
return true;
}
case TRANSACTION_getAccountsByFeatures:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
this.getAccountsByFeatures(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_getPreviousName:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _result = this.getPreviousName(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAccounts:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
android.accounts.Account[] _result = this.getAccounts(_arg0, _arg1);
reply.writeNoException();
reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
return true;
}
case TRANSACTION_getAuthToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String _arg3;
_arg3 = data.readString();
boolean _arg4;
_arg4 = (0!=data.readInt());
boolean _arg5;
_arg5 = (0!=data.readInt());
android.os.Bundle _arg6;
if ((0!=data.readInt())) {
_arg6 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg6 = null;
}
this.getAuthToken(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
return true;
}
case TRANSACTION_setPassword:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
this.setPassword(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_setAuthToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.setAuthToken(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_setUserData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.setUserData(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_hasFeatures:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String[] _arg3;
_arg3 = data.createStringArray();
this.hasFeatures(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_updateCredentials:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String _arg3;
_arg3 = data.readString();
boolean _arg4;
_arg4 = (0!=data.readInt());
android.os.Bundle _arg5;
if ((0!=data.readInt())) {
_arg5 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg5 = null;
}
this.updateCredentials(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_editProperties:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg2;
_arg2 = data.readString();
boolean _arg3;
_arg3 = (0!=data.readInt());
this.editProperties(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_getAuthTokenLabel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.getAuthTokenLabel(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_getUserData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _result = this.getUserData(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPassword:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _result = this.getPassword(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_confirmCredentials:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
boolean _arg4;
_arg4 = (0!=data.readInt());
this.confirmCredentials(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_addAccount:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String[] _arg4;
_arg4 = data.createStringArray();
boolean _arg5;
_arg5 = (0!=data.readInt());
android.os.Bundle _arg6;
if ((0!=data.readInt())) {
_arg6 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg6 = null;
}
this.addAccount(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
return true;
}
case TRANSACTION_addAccountExplicitly:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
boolean _result = this.addAccountExplicitly(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_removeAccountExplicitly:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
boolean _result = this.removeAccountExplicitly(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_renameAccount:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
java.lang.String _arg3;
_arg3 = data.readString();
this.renameAccount(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_removeAccount:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.IAccountManagerResponse _arg1;
_arg1 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
android.accounts.Account _arg2;
if ((0!=data.readInt())) {
_arg2 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
boolean _arg3;
_arg3 = (0!=data.readInt());
this.removeAccount(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_clearPassword:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.clearPassword(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_accountAuthenticated:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
boolean _result = this.accountAuthenticated(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_invalidateAuthToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.invalidateAuthToken(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_peekAuthToken:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
android.accounts.Account _arg1;
if ((0!=data.readInt())) {
_arg1 = android.accounts.Account.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _result = this.peekAuthToken(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lody.virtual.server.IAccountManager
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
@Override public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.accounts.AuthenticatorDescription[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
mRemote.transact(Stub.TRANSACTION_getAuthenticatorTypes, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArray(android.accounts.AuthenticatorDescription.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void getAccountsByFeatures(int userId, android.accounts.IAccountManagerResponse response, java.lang.String type, java.lang.String[] features) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
_data.writeString(type);
_data.writeStringArray(features);
mRemote.transact(Stub.TRANSACTION_getAccountsByFeatures, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getPreviousName(int userId, android.accounts.Account account) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getPreviousName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.accounts.Account[] getAccounts(int userId, java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.accounts.Account[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_getAccounts, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArray(android.accounts.Account.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void getAuthToken(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, android.os.Bundle loginOptions) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(authTokenType);
_data.writeInt(((notifyOnAuthFailure)?(1):(0)));
_data.writeInt(((expectActivityLaunch)?(1):(0)));
if ((loginOptions!=null)) {
_data.writeInt(1);
loginOptions.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getAuthToken, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPassword(int userId, android.accounts.Account account, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_setPassword, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setAuthToken(int userId, android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(authTokenType);
_data.writeString(authToken);
mRemote.transact(Stub.TRANSACTION_setAuthToken, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setUserData(int userId, android.accounts.Account account, java.lang.String key, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(key);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_setUserData, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void hasFeatures(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStringArray(features);
mRemote.transact(Stub.TRANSACTION_hasFeatures, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateCredentials(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean expectActivityLaunch, android.os.Bundle loginOptions) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(authTokenType);
_data.writeInt(((expectActivityLaunch)?(1):(0)));
if ((loginOptions!=null)) {
_data.writeInt(1);
loginOptions.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateCredentials, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void editProperties(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
_data.writeString(accountType);
_data.writeInt(((expectActivityLaunch)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_editProperties, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getAuthTokenLabel(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
_data.writeString(accountType);
_data.writeString(authTokenType);
mRemote.transact(Stub.TRANSACTION_getAuthTokenLabel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getUserData(int userId, android.accounts.Account account, java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_getUserData, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPassword(int userId, android.accounts.Account account) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getPassword, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void confirmCredentials(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, android.os.Bundle options, boolean expectActivityLaunch) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((options!=null)) {
_data.writeInt(1);
options.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((expectActivityLaunch)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_confirmCredentials, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addAccount(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
_data.writeString(accountType);
_data.writeString(authTokenType);
_data.writeStringArray(requiredFeatures);
_data.writeInt(((expectActivityLaunch)?(1):(0)));
if ((optionsIn!=null)) {
_data.writeInt(1);
optionsIn.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addAccount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean addAccountExplicitly(int userId, android.accounts.Account account, java.lang.String password, android.os.Bundle extras) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(password);
if ((extras!=null)) {
_data.writeInt(1);
extras.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addAccountExplicitly, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean removeAccountExplicitly(int userId, android.accounts.Account account) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_removeAccountExplicitly, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void renameAccount(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account accountToRename, java.lang.String newName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((accountToRename!=null)) {
_data.writeInt(1);
accountToRename.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(newName);
mRemote.transact(Stub.TRANSACTION_renameAccount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeAccount(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeStrongBinder((((response!=null))?(response.asBinder()):(null)));
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((expectActivityLaunch)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_removeAccount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void clearPassword(int userId, android.accounts.Account account) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_clearPassword, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean accountAuthenticated(int userId, android.accounts.Account account) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_accountAuthenticated, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void invalidateAuthToken(int userId, java.lang.String accountType, java.lang.String authToken) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
_data.writeString(accountType);
_data.writeString(authToken);
mRemote.transact(Stub.TRANSACTION_invalidateAuthToken, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String peekAuthToken(int userId, android.accounts.Account account, java.lang.String authTokenType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(userId);
if ((account!=null)) {
_data.writeInt(1);
account.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(authTokenType);
mRemote.transact(Stub.TRANSACTION_peekAuthToken, _data, _reply, 0);
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
static final int TRANSACTION_getAuthenticatorTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getAccountsByFeatures = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPreviousName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getAccounts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setUserData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_hasFeatures = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_updateCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_editProperties = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getAuthTokenLabel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getUserData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_confirmCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_addAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_addAccountExplicitly = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_removeAccountExplicitly = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_renameAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_removeAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_clearPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_accountAuthenticated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_invalidateAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_peekAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
}
public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws android.os.RemoteException;
public void getAccountsByFeatures(int userId, android.accounts.IAccountManagerResponse response, java.lang.String type, java.lang.String[] features) throws android.os.RemoteException;
public java.lang.String getPreviousName(int userId, android.accounts.Account account) throws android.os.RemoteException;
public android.accounts.Account[] getAccounts(int userId, java.lang.String type) throws android.os.RemoteException;
public void getAuthToken(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, android.os.Bundle loginOptions) throws android.os.RemoteException;
public void setPassword(int userId, android.accounts.Account account, java.lang.String password) throws android.os.RemoteException;
public void setAuthToken(int userId, android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken) throws android.os.RemoteException;
public void setUserData(int userId, android.accounts.Account account, java.lang.String key, java.lang.String value) throws android.os.RemoteException;
public void hasFeatures(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException;
public void updateCredentials(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean expectActivityLaunch, android.os.Bundle loginOptions) throws android.os.RemoteException;
public void editProperties(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch) throws android.os.RemoteException;
public void getAuthTokenLabel(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType) throws android.os.RemoteException;
public java.lang.String getUserData(int userId, android.accounts.Account account, java.lang.String key) throws android.os.RemoteException;
public java.lang.String getPassword(int userId, android.accounts.Account account) throws android.os.RemoteException;
public void confirmCredentials(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, android.os.Bundle options, boolean expectActivityLaunch) throws android.os.RemoteException;
public void addAccount(int userId, android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn) throws android.os.RemoteException;
public boolean addAccountExplicitly(int userId, android.accounts.Account account, java.lang.String password, android.os.Bundle extras) throws android.os.RemoteException;
public boolean removeAccountExplicitly(int userId, android.accounts.Account account) throws android.os.RemoteException;
public void renameAccount(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account accountToRename, java.lang.String newName) throws android.os.RemoteException;
public void removeAccount(int userId, android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch) throws android.os.RemoteException;
public void clearPassword(int userId, android.accounts.Account account) throws android.os.RemoteException;
public boolean accountAuthenticated(int userId, android.accounts.Account account) throws android.os.RemoteException;
public void invalidateAuthToken(int userId, java.lang.String accountType, java.lang.String authToken) throws android.os.RemoteException;
public java.lang.String peekAuthToken(int userId, android.accounts.Account account, java.lang.String authTokenType) throws android.os.RemoteException;
}
