#include "josh127/libwin32/libwin32.hh"
#include "josh127/libwin32/jni/dev_josh127_libwin32_ComObject.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_PodStruct.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_LibWin32Startup.h"
#include <jni.h>
#include <Windows.h>
#include <Unknwn.h>

jclass NativePtr::clazz;
jfieldID NativePtr::addressID;
jmethodID NativePtr::constructorID;

jclass HResultException::clazz;
jmethodID HResultException::constructorID;

JavaException IllegalArgumentException;

JNIEXPORT jint JNICALL Java_dev_josh127_libwin32_LibWin32Startup_nativeInit
  (JNIEnv * env, jclass clazz)
{
    NativePtr::Init(env, "dev/josh127/libwin32/NativePtr");
    NativeClass<RECT>::Init(env, "dev/josh127/libwin32/Rect");
    HResultException::Init(env, "dev/josh127/libwin32/HResultException");
    IllegalArgumentException.Init(env, "java/lang/IllegalArgumentException");
    return -1;
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_ComObject_release
  (JNIEnv *env, jobject self)
{
    NativePtr::GetValuePtr<IUnknown>(env, self)->Release();
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_PodStruct_free
  (JNIEnv *env, jobject self)
{
    VirtualFree(NativePtr::GetValue<LPVOID>(env, self), 0, MEM_RELEASE);
}