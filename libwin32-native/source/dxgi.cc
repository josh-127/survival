#include "josh127/libwin32/libwin32.hh"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi_ModeDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi_Rational.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi_SampleDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi_SwapChain.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_Dxgi_SwapChainDesc.h"
#include <d3d11.h>
#include <jni.h>

JNIEXPORT jint JNICALL Java_dev_josh127_libwin32_Dxgi_nativeInit
  (JNIEnv *env, jclass clazz)
{
    NativeClass<DXGI_RATIONAL>::Init(env, "dev/josh127/libwin32/Dxgi$Rational");
    NativeClass<DXGI_MODE_DESC>::Init(env, "dev/josh127/libwin32/Dxgi$ModeDesc");
    NativeClass<DXGI_SAMPLE_DESC>::Init(env, "dev/josh127/libwin32/Dxgi$SampleDesc");
    NativeClass<DXGI_SWAP_CHAIN_DESC>::Init(env, "dev/josh127/libwin32/Dxgi$SwapChainDesc");
    NativeClass<IDXGISwapChain>::Init(env, "dev/josh127/libwin32/Dxgi$SwapChain");
    return -1;
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_Dxgi_00024Rational_create
  (JNIEnv *env, jclass clazz, jint numerator, jint denominator)
{
    auto obj = NativeClass<DXGI_RATIONAL>::Alloc();
    obj->Numerator = numerator;
    obj->Denominator = denominator;
    return NativeClass<DXGI_RATIONAL>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_Dxgi_00024ModeDesc_create
  (   JNIEnv *env,
      jclass clazz,
      jint width,
      jint height,
      jobject refreshRate,
      jint format,
      jint scanlineOrdering,
      jint scaling)
{
    auto obj = NativeClass<DXGI_MODE_DESC>::Alloc();
    obj->Width = width;
    obj->Height = height;
    obj->RefreshRate = *NativeClass<DXGI_RATIONAL>::GetValuePtr(env, refreshRate);
    obj->Format = static_cast<DXGI_FORMAT>(format);
    obj->ScanlineOrdering = static_cast<DXGI_MODE_SCANLINE_ORDER>(scanlineOrdering);
    obj->Scaling = static_cast<DXGI_MODE_SCALING>(scaling);
    return NativeClass<DXGI_MODE_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_Dxgi_00024SampleDesc_create
  (JNIEnv *env, jclass clazz, jint count, jint quality)
{
    auto obj = NativeClass<DXGI_SAMPLE_DESC>::Alloc();
    obj->Count = count;
    obj->Quality = quality;
    return NativeClass<DXGI_SAMPLE_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_Dxgi_00024SwapChainDesc_create
  (   JNIEnv *env,
      jclass clazz,
      jobject bufferDesc,
      jobject sampleDesc,
      jint bufferUsage,
      jint bufferCount,
      jobject outputWindow,
      jboolean windowed,
      jint swapEffect,
      jint flags)
{
    auto obj = NativeClass<DXGI_SWAP_CHAIN_DESC>::Alloc();
    obj->BufferDesc = *NativeClass<DXGI_MODE_DESC>::GetValuePtr(env, bufferDesc);
    obj->SampleDesc = *NativeClass<DXGI_SAMPLE_DESC>::GetValuePtr(env, sampleDesc);
    obj->BufferUsage = static_cast<DXGI_USAGE>(bufferUsage);
    obj->BufferCount = bufferCount;
    obj->OutputWindow = NativePtr::GetValue<HWND>(env, outputWindow);
    obj->Windowed = windowed;
    obj->SwapEffect = static_cast<DXGI_SWAP_EFFECT>(swapEffect);
    obj->Flags = flags;
    return NativeClass<DXGI_SWAP_CHAIN_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_Dxgi_00024SwapChain_getBuffer
  (JNIEnv *env, jobject self, jint buffer)
{
    ID3D11Texture2D* obj = nullptr;
    NativeClass<IDXGISwapChain>::GetValuePtr(env, self)->GetBuffer(buffer, IID_ID3D11Texture2D, (void**) &obj);
    return NativeClass<ID3D11Texture2D>::Create(env, obj);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_Dxgi_00024SwapChain_present
  (JNIEnv *env, jobject self, jint syncInterval, jint flags)
{
    auto result = NativeClass<IDXGISwapChain>::GetValuePtr(env, self)->Present(syncInterval, flags);

    if (result != S_OK) env->Throw(HResultException::Create(env, "IDXGISwapChain::Present", result));
}