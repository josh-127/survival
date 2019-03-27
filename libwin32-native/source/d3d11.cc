#include "josh127/libwin32/libwin32.hh"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_BlendDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_Box.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_BufferDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_DepthStencilDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_DepthStencilOpDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_Device.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_DeviceContext.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_DeviceDeviceContextSwapChainTuple.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_InputElementDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_MappedSubresource.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_RasterizerDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_RenderTargetBlendDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_SamplerDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_SubresourceData.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_Texture2D.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_Texture2DDesc.h"
#include "josh127/libwin32/jni/dev_josh127_libwin32_D3D11_Viewport.h"
#include <d3d11.h>
#include <jni.h>

JNIEXPORT jint JNICALL Java_dev_josh127_libwin32_D3D11_nativeInit(JNIEnv* env, jclass clazz)
{
    NativeClass<D3D11_INPUT_ELEMENT_DESC>::Init(env, "dev/josh127/libwin32/D3D11$InputElementDesc");
    NativeClass<D3D11_VIEWPORT>::Init(env, "dev/josh127/libwin32/D3D11$Viewport");
    NativeClass<D3D11_BOX>::Init(env, "dev/josh127/libwin32/D3D11$Box");
    NativeClass<D3D11_DEPTH_STENCILOP_DESC>::Init(env, "dev/josh127/libwin32/D3D11$DepthStencilOpDesc");
    NativeClass<D3D11_DEPTH_STENCIL_DESC>::Init(env, "dev/josh127/libwin32/D3D11$DepthStencilDesc");
    NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::Init(env, "dev/josh127/libwin32/D3D11$RenderTargetBlendDesc");
    NativeClass<D3D11_BLEND_DESC>::Init(env, "dev/josh127/libwin32/D3D11$BlendDesc");
    NativeClass<D3D11_RASTERIZER_DESC>::Init(env, "dev/josh127/libwin32/D3D11$RasterizerDesc");
    NativeClass<D3D11_SUBRESOURCE_DATA>::Init(env, "dev/josh127/libwin32/D3D11$SubresourceData");
    NativeClass<D3D11_MAPPED_SUBRESOURCE>::Init(env, "dev/josh127/libwin32/D3D11$MappedSubresource");
    NativeClass<D3D11_BUFFER_DESC>::Init(env, "dev/josh127/libwin32/D3D11$BufferDesc");
    NativeClass<D3D11_TEXTURE2D_DESC>::Init(env, "dev/josh127/libwin32/D3D11$Texture2DDesc");
    NativeClass<D3D11_RENDER_TARGET_VIEW_DESC>::Init(env, "dev/josh127/libwin32/D3D11$RenderTargetViewDesc");
    NativeClass<D3D11_DEPTH_STENCIL_VIEW_DESC>::Init(env, "dev/josh127/libwin32/D3D11$DepthStencilViewDesc");
    NativeClass<D3D11_SAMPLER_DESC>::Init(env, "dev/josh127/libwin32/D3D11$SamplerDesc");
    NativeClass<ID3D11DepthStencilState>::Init(env, "dev/josh127/libwin32/D3D11$DepthStencilState");
    NativeClass<ID3D11BlendState>::Init(env, "dev/josh127/libwin32/D3D11$BlendState");
    NativeClass<ID3D11RasterizerState>::Init(env, "dev/josh127/libwin32/D3D11$RasterizerState");
    NativeClass<ID3D11Resource>::Init(env, "dev/josh127/libwin32/D3D11$Resource");
    NativeClass<ID3D11Buffer>::Init(env, "dev/josh127/libwin32/D3D11$Buffer");
    NativeClass<ID3D11Texture2D>::Init(env, "dev/josh127/libwin32/D3D11$Texture2D");
    NativeClass<ID3D11View>::Init(env, "dev/josh127/libwin32/D3D11$View");
    NativeClass<ID3D11ShaderResourceView>::Init(env, "dev/josh127/libwin32/D3D11$ShaderResourceView");
    NativeClass<ID3D11RenderTargetView>::Init(env, "dev/josh127/libwin32/D3D11$RenderTargetView");
    NativeClass<ID3D11DepthStencilView>::Init(env, "dev/josh127/libwin32/D3D11$DepthStencilView");
    NativeClass<ID3D11VertexShader>::Init(env, "dev/josh127/libwin32/D3D11$VertexShader");
    NativeClass<ID3D11PixelShader>::Init(env, "dev/josh127/libwin32/D3D11$PixelShader");
    NativeClass<ID3D11InputLayout>::Init(env, "dev/josh127/libwin32/D3D11$InputLayout");
    NativeClass<ID3D11SamplerState>::Init(env, "dev/josh127/libwin32/D3D11$SamplerState");
    NativeClass<ID3D11Device>::Init(env, "dev/josh127/libwin32/D3D11$Device");
    NativeClass<ID3D11DeviceContext>::Init(env, "dev/josh127/libwin32/D3D11$DeviceContext");
    NativeClass<DeviceDeviceContextSwapChainTuple>::Init(env, "dev/josh127/libwin32/D3D11$DeviceDeviceContextSwapChainTuple");

    return -1;
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024InputElementDesc_create(
    JNIEnv* env,
    jclass clazz,
    jstring semanticName,
    jint semanticIndex,
    jint format,
    jint inputSlot,
    jint alignedByteOffset,
    jint inputSlotClass,
    jint instanceDataStepRate)
{
    // TODO: Check if semanticNameChars works after being freed.
    auto obj = NativeClass<D3D11_INPUT_ELEMENT_DESC>::Alloc();
    const char* semanticNameChars = env->GetStringUTFChars(semanticName, nullptr);
    obj->SemanticName = semanticNameChars;
    obj->SemanticIndex = semanticIndex;
    obj->Format = static_cast<DXGI_FORMAT>(format);
    obj->InputSlot = inputSlot;
    obj->AlignedByteOffset = alignedByteOffset;
    obj->InputSlotClass = static_cast<D3D11_INPUT_CLASSIFICATION>(inputSlotClass);
    obj->InstanceDataStepRate = instanceDataStepRate;
    //env->ReleaseStringUTFChars(semanticName, semanticNameChars);
    return NativeClass<D3D11_INPUT_ELEMENT_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Viewport_create(
    JNIEnv* env,
    jclass clazz,
    jfloat topLeftX,
    jfloat topLeftY,
    jfloat width,
    jfloat height,
    jfloat minDepth,
    jfloat maxDepth)
{
    auto obj = NativeClass<D3D11_VIEWPORT>::Alloc();
    obj->TopLeftX = topLeftX;
    obj->TopLeftY = topLeftY;
    obj->Width = width;
    obj->Height = height;
    obj->MinDepth = minDepth;
    obj->MaxDepth = maxDepth;
    return NativeClass<D3D11_VIEWPORT>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Box_create
    (   JNIEnv* env,
        jclass clazz,
        jint left,
        jint top,
        jint front,
        jint right,
        jint bottom,
        jint back)
{
    auto obj = NativeClass<D3D11_BOX>::Alloc();
    obj->left = left;
    obj->top = top;
    obj->front = front;
    obj->right = right;
    obj->bottom = bottom;
    obj->back = back;
    return NativeClass<D3D11_BOX>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DepthStencilOpDesc_create
    (   JNIEnv* env,
        jclass clazz,
        jint stencilFailOp,
        jint stencilDepthFailOp,
        jint stencilPassOp,
        jint stencilFunc)
{
    auto obj = NativeClass<D3D11_DEPTH_STENCILOP_DESC>::Alloc();
    obj->StencilFailOp = static_cast<D3D11_STENCIL_OP>(stencilFailOp);
    obj->StencilDepthFailOp = static_cast<D3D11_STENCIL_OP>(stencilDepthFailOp);
    obj->StencilPassOp = static_cast<D3D11_STENCIL_OP>(stencilPassOp);
    obj->StencilFunc = static_cast<D3D11_COMPARISON_FUNC>(stencilFunc);
    return NativeClass<D3D11_DEPTH_STENCILOP_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DepthStencilDesc_create(
    JNIEnv* env,
    jclass clazz,
    jboolean depthEnable,
    jint depthWriteMask,
    jint depthFunc,
    jboolean stencilEnable,
    jbyte stencilReadMask,
    jbyte stencilWriteMask,
    jobject frontFace,
    jobject backFace)
{
    auto obj = NativeClass<D3D11_DEPTH_STENCIL_DESC>::Alloc();
    obj->DepthEnable = depthEnable;
    obj->DepthWriteMask = static_cast<D3D11_DEPTH_WRITE_MASK>(depthWriteMask);
    obj->DepthFunc = static_cast<D3D11_COMPARISON_FUNC>(depthFunc);
    obj->StencilEnable = stencilEnable;
    obj->StencilReadMask = stencilReadMask;
    obj->StencilWriteMask = stencilWriteMask;
    obj->FrontFace = *NativeClass<D3D11_DEPTH_STENCILOP_DESC>::GetValuePtr(env, frontFace);
    obj->BackFace = *NativeClass<D3D11_DEPTH_STENCILOP_DESC>::GetValuePtr(env, backFace);
    return NativeClass<D3D11_DEPTH_STENCIL_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024RenderTargetBlendDesc_create(
    JNIEnv* env,
    jclass clazz,
    jboolean blendEnable,
    jint srcBlend,
    jint destBlend,
    jint blendOp,
    jint srcBlendAlpha,
    jint destBlendAlpha,
    jint blendOpAlpha,
    jbyte renderTargetWriteMask)
{
    auto obj = NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::Alloc();
    obj->BlendEnable = blendEnable;
    obj->SrcBlend = static_cast<D3D11_BLEND>(srcBlend);
    obj->DestBlend = static_cast<D3D11_BLEND>(destBlend);
    obj->BlendOp = static_cast<D3D11_BLEND_OP>(blendOp);
    obj->SrcBlendAlpha = static_cast<D3D11_BLEND>(srcBlendAlpha);
    obj->DestBlendAlpha = static_cast<D3D11_BLEND>(destBlendAlpha);
    obj->BlendOpAlpha = static_cast<D3D11_BLEND_OP>(blendOpAlpha);
    obj->RenderTargetWriteMask = renderTargetWriteMask;
    return NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024BlendDesc_create(
    JNIEnv* env,
    jclass clazz,
    jboolean alphaToCoverageEnable,
    jboolean independentBlendEnable,
    jobjectArray renderTarget)
{
    auto obj = NativeClass<D3D11_BLEND_DESC>::Alloc();
    obj->AlphaToCoverageEnable = alphaToCoverageEnable;
    obj->IndependentBlendEnable = independentBlendEnable;

#if 0
    if (NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::GetArrayLength(env, renderTarget) != 8) {
        env->Throw(IllegalArgumentException.Create(env, "renderTarget.length == 8"));
    }
    else
#endif
    {
        auto targetField = NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::AllocArray(8);
        auto length = NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::GetArrayLength(env, renderTarget);
        NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::GetArray(env, renderTarget, targetField);

        for (auto i = 0; i < length; i++) {
            obj->RenderTarget[i] = targetField[i];
        }

        for (auto i = length; i < 8; i++) {
            obj->RenderTarget[i] = {};
        }

        NativeClass<D3D11_RENDER_TARGET_BLEND_DESC>::FreeArray(targetField);
    }

    return NativeClass<D3D11_BLEND_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024RasterizerDesc_create(
    JNIEnv* env,
    jclass clazz,
    jint fillMode,
    jint cullMode,
    jboolean frontCounterClockwise,
    jint depthBias,
    jfloat depthBiasClamp,
    jfloat slopeScaledDepthBias,
    jboolean depthClipEnable,
    jboolean scissorEnable,
    jboolean multisampleEnable,
    jboolean antialiasedLineEnable)
{
    auto obj = NativeClass<D3D11_RASTERIZER_DESC>::Alloc();
    obj->FillMode = static_cast<D3D11_FILL_MODE>(fillMode);
    obj->CullMode = static_cast<D3D11_CULL_MODE>(cullMode);
    obj->FrontCounterClockwise = frontCounterClockwise;
    obj->DepthBias = depthBias;
    obj->DepthBiasClamp = depthBiasClamp;
    obj->SlopeScaledDepthBias = slopeScaledDepthBias;
    obj->DepthClipEnable = depthClipEnable;
    obj->ScissorEnable = scissorEnable;
    obj->MultisampleEnable = multisampleEnable;
    obj->AntialiasedLineEnable = antialiasedLineEnable;
    return NativeClass<D3D11_RASTERIZER_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024SubresourceData_create
    (   JNIEnv* env,
        jclass clazz,
        jobject sysMem,
        jint sysMemPitch,
        jint sysMemSlicePitch)
{
    auto obj = NativeClass<D3D11_SUBRESOURCE_DATA>::Alloc();
    obj->pSysMem = env->GetDirectBufferAddress(sysMem);
    obj->SysMemPitch = sysMemPitch;
    obj->SysMemSlicePitch = sysMemSlicePitch;
    return NativeClass<D3D11_SUBRESOURCE_DATA>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024MappedSubresource_create(
    JNIEnv* env,
    jclass clazz,
    jobject data,
    jint rowPitch,
    jint depthPitch)
{
    auto obj = NativeClass<D3D11_MAPPED_SUBRESOURCE>::Alloc();
    obj->pData = env->GetDirectBufferAddress(data);
    obj->RowPitch = rowPitch;
    obj->DepthPitch = depthPitch;
    return NativeClass<D3D11_MAPPED_SUBRESOURCE>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024MappedSubresource_getData(
    JNIEnv* env,
    jobject self)
{
    return CreateDirectByteBuffer(
        env, NativeClass<D3D11_MAPPED_SUBRESOURCE>::GetValuePtr(env, self)->pData, 0x7FFFFFFF);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024BufferDesc_create(
    JNIEnv* env,
    jclass clazz,
    jint byteWidth,
    jint usage,
    jint bindFlags,
    jint cpuAccessFlags,
    jint miscFlags,
    jint structureByteStride)
{
    auto obj = NativeClass<D3D11_BUFFER_DESC>::Alloc();
    obj->ByteWidth = byteWidth;
    obj->Usage = static_cast<D3D11_USAGE>(usage);
    obj->BindFlags = bindFlags;
    obj->CPUAccessFlags = cpuAccessFlags;
    obj->MiscFlags = miscFlags;
    obj->StructureByteStride = structureByteStride;
    return NativeClass<D3D11_BUFFER_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Texture2DDesc_create(
    JNIEnv* env,
    jclass clazz,
    jint width,
    jint height,
    jint mipLevels,
    jint arraySize,
    jint format,
    jobject sampleDesc,
    jint usage,
    jint bindFlags,
    jint cpuAccessFlags,
    jint miscFlags)
{
    auto obj = NativeClass<D3D11_TEXTURE2D_DESC>::Alloc();
    obj->Width = width;
    obj->Height = height;
    obj->MipLevels = mipLevels;
    obj->ArraySize = arraySize;
    obj->Format = static_cast<DXGI_FORMAT>(format);
    obj->SampleDesc = *NativeClass<DXGI_SAMPLE_DESC>::GetValuePtr(env, sampleDesc);
    obj->Usage = static_cast<D3D11_USAGE>(usage);
    obj->BindFlags = bindFlags;
    obj->CPUAccessFlags = cpuAccessFlags;
    obj->MiscFlags = miscFlags;
    return NativeClass<D3D11_TEXTURE2D_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024SamplerDesc_create(
    JNIEnv* env,
    jclass clazz,
    jint filter,
    jint addressU,
    jint addressV,
    jint addressW,
    jfloat mipLodBias,
    jint maxAnisotropy,
    jint comparisonFunc,
    jfloatArray borderColor,
    jfloat minLOD,
    jfloat maxLOD)
{
    auto obj = NativeClass<D3D11_SAMPLER_DESC>::Alloc();
    obj->Filter = static_cast<D3D11_FILTER>(filter);
    obj->AddressU = static_cast<D3D11_TEXTURE_ADDRESS_MODE>(addressU);
    obj->AddressV = static_cast<D3D11_TEXTURE_ADDRESS_MODE>(addressV);
    obj->AddressW = static_cast<D3D11_TEXTURE_ADDRESS_MODE>(addressW);
    obj->MipLODBias = mipLodBias;
    obj->MaxAnisotropy = maxAnisotropy;
    obj->ComparisonFunc = static_cast<D3D11_COMPARISON_FUNC>(comparisonFunc);
    obj->MinLOD = minLOD;
    obj->MaxLOD = maxLOD;

    if (borderColor == nullptr) {
        obj->BorderColor[0] = 0.0f;
        obj->BorderColor[1] = 0.0f;
        obj->BorderColor[2] = 0.0f;
        obj->BorderColor[3] = 0.0f;
    }
    else {
        if (env->GetArrayLength(borderColor) != 4) {
            env->Throw(IllegalArgumentException.Create(env, "borderColor.length == 4"));
        }
        else {
            float* colorField = env->GetFloatArrayElements(borderColor, nullptr);
            obj->BorderColor[0] = colorField[0];
            obj->BorderColor[1] = colorField[1];
            obj->BorderColor[2] = colorField[2];
            obj->BorderColor[3] = colorField[3];
            env->ReleaseFloatArrayElements(borderColor, colorField, 0);
        }
    }

    return NativeClass<D3D11_SAMPLER_DESC>::Create(env, obj);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Texture2D_getDesc(
    JNIEnv* env,
    jobject self)
{
    D3D11_TEXTURE2D_DESC* desc = nullptr;
    NativeClass<ID3D11Texture2D>::GetValuePtr(env, self)->GetDesc(desc);
    return NativeClass<D3D11_TEXTURE2D_DESC>::Create(env, desc);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createBlendState
    (   JNIEnv* env,
        jobject self,
        jobject blendStateDesc)
{
    ID3D11BlendState* ret = nullptr;
    auto result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateBlendState(
        NativeClass<D3D11_BLEND_DESC>::GetValuePtr(env, blendStateDesc), &ret);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateBlendState", result));
    }

    return NativeClass<ID3D11BlendState>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createBuffer
    (   JNIEnv* env,
        jobject self,
        jobject desc,
        jobject initialData)
{
    ID3D11Buffer* ret = nullptr;
    HRESULT result;

    if (initialData == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateBuffer(
            NativeClass<D3D11_BUFFER_DESC>::GetValuePtr(env, desc), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateBuffer(
            NativeClass<D3D11_BUFFER_DESC>::GetValuePtr(env, desc),
            NativeClass<D3D11_SUBRESOURCE_DATA>::GetValuePtr(env, initialData),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateBuffer", result));
    }

    return NativeClass<ID3D11Buffer>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createDepthStencilState
    (   JNIEnv* env,
        jobject self,
        jobject depthStencilDesc)
{
    ID3D11DepthStencilState* ret = nullptr;
    auto result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateDepthStencilState(
        NativeClass<D3D11_DEPTH_STENCIL_DESC>::GetValuePtr(env, depthStencilDesc), &ret);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateDepthStencilState", result));
    }

    return NativeClass<ID3D11DepthStencilState>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createDepthStencilView
    (   JNIEnv* env,
        jobject self,
        jobject resource,
        jobject desc)
{
    ID3D11DepthStencilView* ret = nullptr;
    HRESULT result;

    if (desc == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateDepthStencilView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateDepthStencilView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource),
            NativeClass<D3D11_DEPTH_STENCIL_VIEW_DESC>::GetValuePtr(env, desc),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateDepthStencilView", result));
    }

    return NativeClass<ID3D11DepthStencilView>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createInputLayout
    (   JNIEnv* env,
        jobject self,
        jobjectArray inputElementDescs,
        jobject shaderBytecodeWithInputSignature)
{
    auto descs = NativeClass<D3D11_INPUT_ELEMENT_DESC>::AllocArray(env, inputElementDescs);
    NativeClass<D3D11_INPUT_ELEMENT_DESC>::GetArray(env, inputElementDescs, descs);

    ID3D11InputLayout* ret = nullptr;

    auto result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateInputLayout(
        descs,
        NativeClass<D3D11_INPUT_ELEMENT_DESC>::GetArrayLength(env, inputElementDescs),
        env->GetDirectBufferAddress(shaderBytecodeWithInputSignature),
        env->GetDirectBufferCapacity(shaderBytecodeWithInputSignature),
        &ret);

    NativeClass<D3D11_INPUT_ELEMENT_DESC>::FreeArray(descs);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateInputLayout", result));
    }

    return NativeClass<ID3D11InputLayout>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createPixelShader
    (   JNIEnv* env,
        jobject self,
        jobject shaderBytecode,
        jobject classLinkage)
{
    ID3D11PixelShader* ret = nullptr;
    HRESULT result;

    if (classLinkage == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreatePixelShader(
            env->GetDirectBufferAddress(shaderBytecode), env->GetDirectBufferCapacity(shaderBytecode), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreatePixelShader(
            env->GetDirectBufferAddress(shaderBytecode),
            env->GetDirectBufferCapacity(shaderBytecode),
            NativeClass<ID3D11ClassLinkage>::GetValuePtr(env, classLinkage),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreatePixelShader", result));
    }

    return NativeClass<ID3D11PixelShader>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createRasterizerState
    (   JNIEnv* env,
        jobject self,
        jobject rasterizerDesc)
{
    ID3D11RasterizerState* ret = nullptr;

    auto result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateRasterizerState(
        NativeClass<D3D11_RASTERIZER_DESC>::GetValuePtr(env, rasterizerDesc), &ret);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateRasterizerState", result));
    }

    return NativeClass<ID3D11RasterizerState>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createRenderTargetView
    (   JNIEnv* env,
        jobject self,
        jobject resource,
        jobject desc)
{
    ID3D11RenderTargetView* ret = nullptr;
    HRESULT result;

    if (desc == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateRenderTargetView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateRenderTargetView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource),
            NativeClass<D3D11_RENDER_TARGET_VIEW_DESC>::GetValuePtr(env, desc),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateRenderTargetView", result));
    }

    return NativeClass<ID3D11RenderTargetView>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createSamplerState
    (   JNIEnv* env,
        jobject self,
        jobject samplerDesc)
{
    ID3D11SamplerState* ret = nullptr;
    auto result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateSamplerState(
        NativeClass<D3D11_SAMPLER_DESC>::GetValuePtr(env, samplerDesc), &ret);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateSamplerState", result));
    }

    return NativeClass<ID3D11SamplerState>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createShaderResourceView
    (   JNIEnv* env,
        jobject self,
        jobject resource,
        jobject desc)
{
    ID3D11ShaderResourceView* ret = nullptr;
    HRESULT result;

    if (desc == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateShaderResourceView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateShaderResourceView(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource),
            NativeClass<D3D11_SHADER_RESOURCE_VIEW_DESC>::GetValuePtr(env, desc),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateShaderResourceView", result));
    }

    return NativeClass<ID3D11ShaderResourceView>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createTexture2D
    (   JNIEnv* env,
        jobject self,
        jobject desc,
        jobject initialData)
{
    ID3D11Texture2D* ret = nullptr;
    HRESULT result;

    if (initialData == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateTexture2D(
            NativeClass<D3D11_TEXTURE2D_DESC>::GetValuePtr(env, desc), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateTexture2D(
            NativeClass<D3D11_TEXTURE2D_DESC>::GetValuePtr(env, desc),
            NativeClass<D3D11_SUBRESOURCE_DATA>::GetValuePtr(env, initialData),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateTexture2D", result));
    }

    return NativeClass<ID3D11Texture2D>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_createVertexShader
    (   JNIEnv* env,
        jobject self,
        jobject shaderBytecode,
        jobject classLinkage)
{
    ID3D11VertexShader* ret = nullptr;
    HRESULT result;

    if (classLinkage == nullptr) {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateVertexShader(
            env->GetDirectBufferAddress(shaderBytecode), env->GetDirectBufferCapacity(shaderBytecode), nullptr, &ret);
    }
    else {
        result = NativeClass<ID3D11Device>::GetValuePtr(env, self)->CreateVertexShader(
            env->GetDirectBufferAddress(shaderBytecode),
            env->GetDirectBufferCapacity(shaderBytecode),
            NativeClass<ID3D11ClassLinkage>::GetValuePtr(env, classLinkage),
            &ret);
    }

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "ID3D11Device::CreateVertexShader", result));
    }

    return NativeClass<ID3D11VertexShader>::Create(env, ret);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024Device_getImmediateContext(
    JNIEnv* env,
    jobject self)
{
    ID3D11DeviceContext* context = nullptr;
    NativeClass<ID3D11Device>::GetValuePtr(env, self)->GetImmediateContext(&context);
    return NativeClass<ID3D11DeviceContext>::Create(env, context);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_clearDepthStencilView
    (   JNIEnv* env,
        jobject self,
        jobject depthStencilView,
        jint clearFlags,
        jfloat depth,
        jbyte stencil)
{
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->ClearDepthStencilView(
        NativeClass<ID3D11DepthStencilView>::GetValuePtr(env, depthStencilView), clearFlags, depth, stencil);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_clearRenderTargetView
    (   JNIEnv* env,
        jobject self,
        jobject renderTargetView,
        jfloat r,
        jfloat g,
        jfloat b,
        jfloat a)
{
    float rgba[] = { r, g, b, a };
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->ClearRenderTargetView(
        NativeClass<ID3D11RenderTargetView>::GetValuePtr(env, renderTargetView), rgba);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_draw
    (   JNIEnv* env,
        jobject self,
        jint vertexCount,
        jint startVertexLocation)
{
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->Draw(vertexCount, startVertexLocation);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_drawIndexed
    (   JNIEnv* env,
        jobject self,
        jint indexCount,
        jint startIndexLocation,
        jint baseVertexLocation)
{
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->DrawIndexed(
        indexCount, startIndexLocation, baseVertexLocation);
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_generateMips
    (   JNIEnv* env,
        jobject self,
        jobject shaderResourceView)
{
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->GenerateMips(
        NativeClass<ID3D11ShaderResourceView>::GetValuePtr(env, shaderResourceView));
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_iaSetIndexBuffer
    (   JNIEnv* env,
        jobject self,
        jobject indexBuffer,
        jint format,
        jint offset)
{
    if (indexBuffer == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetIndexBuffer(
            nullptr, static_cast<DXGI_FORMAT>(format), offset);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetIndexBuffer(
            NativeClass<ID3D11Buffer>::GetValuePtr(env, indexBuffer), static_cast<DXGI_FORMAT>(format), offset);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_iaSetInputLayout
    (   JNIEnv* env,
        jobject self,
        jobject inputLayout)
{
    if (inputLayout == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetInputLayout(nullptr);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetInputLayout(
            NativeClass<ID3D11InputLayout>::GetValuePtr(env, inputLayout));
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_iaSetPrimitiveTopology
    (   JNIEnv* env,
        jobject self,
        jint topology)
{
    NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetPrimitiveTopology(
        static_cast<D3D11_PRIMITIVE_TOPOLOGY>(topology));
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_iaSetVertexBuffers
    (   JNIEnv* env,
        jobject self,
        jint startSlot,
        jobjectArray vertexBuffers,
        jintArray strides,
        jintArray offsets)
{
    if (vertexBuffers == nullptr && strides == nullptr && offsets == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetVertexBuffers(
            startSlot, 0, nullptr, nullptr, nullptr);
    }
    else if (strides == nullptr && offsets == nullptr) {
        auto vertexBuffersParam = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, vertexBuffers);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, vertexBuffers, vertexBuffersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetVertexBuffers(
            startSlot, NativeClass<ID3D11Buffer>::GetArrayLength(env, vertexBuffers),
            vertexBuffersParam, nullptr, nullptr);

        NativeClass<ID3D11Buffer>::FreeArrayPtr(vertexBuffersParam);
    }
    else if (strides == nullptr) {
        auto vertexBuffersLength = NativeClass<ID3D11Buffer>::GetArrayLength(env, vertexBuffers);
        auto offsetsLength = env->GetArrayLength(offsets);

        if (vertexBuffersLength != offsetsLength) {
            env->Throw(IllegalArgumentException.Create(env, "vertexBuffers.length == offsets.length"));
        }

        auto vertexBuffersParam = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, vertexBuffers);
        auto offsetsParam = env->GetIntArrayElements(offsets, nullptr);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, vertexBuffers, vertexBuffersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetVertexBuffers(
            startSlot, vertexBuffersLength, vertexBuffersParam, nullptr, reinterpret_cast<UINT*>(offsetsParam));

        env->ReleaseIntArrayElements(offsets, offsetsParam, 0);
        NativeClass<ID3D11Buffer>::FreeArrayPtr(vertexBuffersParam);
    }
    else if (offsets == nullptr) {
        auto vertexBuffersLength = NativeClass<ID3D11Buffer>::GetArrayLength(env, vertexBuffers);
        auto stridesLength = env->GetArrayLength(strides);

        if (vertexBuffersLength != stridesLength) {
            env->Throw(IllegalArgumentException.Create(env, "vertexBuffers.length == strides.length"));
        }

        auto vertexBuffersParam = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, vertexBuffers);
        auto stridesParam = env->GetIntArrayElements(strides, nullptr);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, vertexBuffers, vertexBuffersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetVertexBuffers(
            startSlot, vertexBuffersLength, vertexBuffersParam, reinterpret_cast<UINT*>(stridesParam), nullptr);

        env->ReleaseIntArrayElements(strides, stridesParam, 0);
        NativeClass<ID3D11Buffer>::FreeArrayPtr(vertexBuffersParam);
    }
    else if (vertexBuffers == nullptr) {
        env->Throw(IllegalArgumentException.Create(
            env, "vertexBuffers != null || (vertexBuffers == null && strides == null && offsets == null)"));
    }
    else {
        auto vertexBuffersLength = NativeClass<ID3D11Buffer>::GetArrayLength(env, vertexBuffers);
        auto stridesLength = env->GetArrayLength(strides);
        auto offsetsLength = env->GetArrayLength(offsets);

        if (vertexBuffersLength != stridesLength || vertexBuffersLength != offsetsLength) {
            env->Throw(IllegalArgumentException.Create(
                env, "vertexBuffers.length == strides.length == offsets.length"));
        }

        auto vertexBuffersParam = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, vertexBuffers);
        auto stridesParam = env->GetIntArrayElements(strides, nullptr);
        auto offsetsParam = env->GetIntArrayElements(offsets, nullptr);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, vertexBuffers, vertexBuffersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->IASetVertexBuffers(
            startSlot, vertexBuffersLength, vertexBuffersParam, reinterpret_cast<UINT*>(stridesParam),
            reinterpret_cast<UINT*>(offsetsParam));

        // TODO:
        //env->ReleaseIntArrayElements(offsets, offsetsParam, 0);
        //env->ReleaseIntArrayElements(strides, stridesParam, 0);
        NativeClass<ID3D11Buffer>::FreeArrayPtr(vertexBuffersParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_omSetBlendState
    (   JNIEnv* env,
        jobject self,
        jobject blendState,
        jfloatArray blendFactor,
        jint sampleMask)
{
    local_persist float defaultBlendFactor[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    float* factorParam = blendFactor == nullptr
        ? defaultBlendFactor : env->GetFloatArrayElements(blendFactor, nullptr);

    if (blendState == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetBlendState(nullptr, factorParam, sampleMask);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetBlendState(
            NativeClass<ID3D11BlendState>::GetValuePtr(env, blendState), factorParam, sampleMask);
    }

    if (blendFactor != nullptr) {
        env->ReleaseFloatArrayElements(blendFactor, factorParam, 0);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_omSetDepthStencilState
    (   JNIEnv* env,
        jobject self,
        jobject depthStencilState,
        jint stencilRef)
{
    if (depthStencilState == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetDepthStencilState(nullptr, stencilRef);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetDepthStencilState(
            NativeClass<ID3D11DepthStencilState>::GetValuePtr(env, depthStencilState), stencilRef);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_omSetRenderTargets
    (   JNIEnv* env,
        jobject self,
        jobjectArray renderTargetViews,
        jobject depthStencilView)
{
    if (renderTargetViews == nullptr && depthStencilView == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetRenderTargets(0, nullptr, nullptr);
    }
    else if (renderTargetViews == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetRenderTargets(
            0, nullptr, NativeClass<ID3D11DepthStencilView>::GetValuePtr(env, depthStencilView));
    }
    else if (depthStencilView == nullptr) {
        auto viewsParam = NativeClass<ID3D11RenderTargetView>::AllocArrayPtr(env, renderTargetViews);
        NativeClass<ID3D11RenderTargetView>::GetArrayPtr(env, renderTargetViews, viewsParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetRenderTargets(
            NativeClass<ID3D11RenderTargetView>::GetArrayLength(env, renderTargetViews), viewsParam, nullptr);

        NativeClass<ID3D11RenderTargetView>::FreeArrayPtr(viewsParam);
    }
    else {
        auto viewsParam = NativeClass<ID3D11RenderTargetView>::AllocArrayPtr(env, renderTargetViews);
        NativeClass<ID3D11RenderTargetView>::GetArrayPtr(env, renderTargetViews, viewsParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->OMSetRenderTargets(
            NativeClass<ID3D11RenderTargetView>::GetArrayLength(env, renderTargetViews), viewsParam,
            NativeClass<ID3D11DepthStencilView>::GetValuePtr(env, depthStencilView));

        NativeClass<ID3D11RenderTargetView>::FreeArrayPtr(viewsParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_psSetConstantBuffers
    (   JNIEnv* env,
        jobject self,
        jint startSlot,
        jobjectArray constantBuffers)
{
    if (constantBuffers == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetConstantBuffers(startSlot, 0, nullptr);
    }
    else {
        auto buffers = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, constantBuffers);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, constantBuffers, buffers);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetConstantBuffers(
            startSlot, NativeClass<ID3D11Buffer>::GetArrayLength(env, constantBuffers), buffers);

        NativeClass<ID3D11Buffer>::FreeArrayPtr(buffers);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_psSetSamplers
    (   JNIEnv* env,
        jobject self,
        jint startSlot,
        jobjectArray samplers)
{
    if (samplers == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetSamplers(startSlot, 0, nullptr);
    }
    else {
        auto samplersParam = NativeClass<ID3D11SamplerState>::AllocArrayPtr(env, samplers);
        NativeClass<ID3D11SamplerState>::GetArrayPtr(env, samplers, samplersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetSamplers(
            startSlot, NativeClass<ID3D11SamplerState>::GetArrayLength(env, samplers), samplersParam);

        NativeClass<ID3D11SamplerState>::FreeArrayPtr(samplersParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_psSetShader
    (   JNIEnv* env,
        jobject self,
        jobject pixelShader,
        jobjectArray classInstances)
{
    if (pixelShader == nullptr && classInstances == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetShader(nullptr, nullptr, 0);
    }
    else if (classInstances == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetShader(
            NativeClass<ID3D11PixelShader>::GetValuePtr(env, pixelShader), nullptr, 0);
    }
    else if (pixelShader == nullptr) {
        env->Throw(IllegalArgumentException.Create(
            env, "pixelShader != null || (pixelShader == null && classInstances == null"));
    }
    else {
        auto instances = NativeClass<ID3D11ClassInstance>::AllocArrayPtr(env, classInstances);
        NativeClass<ID3D11ClassInstance>::GetArrayPtr(env, classInstances, instances);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetShader(
            NativeClass<ID3D11PixelShader>::GetValuePtr(env, pixelShader),
            instances,
            NativeClass<ID3D11ClassInstance>::GetArrayLength(env, classInstances));

        NativeClass<ID3D11ClassInstance>::FreeArrayPtr(instances);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_psSetShaderResources
    (   JNIEnv* env,
        jobject self,
        jint startSlot,
        jobjectArray shaderResourceViews)
{
    if (shaderResourceViews == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetShaderResources(startSlot, 0, nullptr);
    }
    else {
        auto views = NativeClass<ID3D11ShaderResourceView>::AllocArrayPtr(env, shaderResourceViews);
        NativeClass<ID3D11ShaderResourceView>::GetArrayPtr(env, shaderResourceViews, views);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->PSSetShaderResources(
            startSlot, NativeClass<ID3D11ShaderResourceView>::GetArrayLength(env, shaderResourceViews), views);

        NativeClass<ID3D11ShaderResourceView>::FreeArrayPtr(views);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_rsSetScissorRects
    (   JNIEnv* env,
        jobject self,
        jobjectArray rects)
{
    if (rects == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetScissorRects(0, nullptr);
    }
    else {
        auto rectsParam = NativeClass<RECT>::AllocArray(env, rects);
        NativeClass<RECT>::GetArray(env, rects, rectsParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetScissorRects(
            NativeClass<RECT>::GetArrayLength(env, rects), rectsParam);

        NativeClass<RECT>::FreeArray(rectsParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_rsSetState
    (   JNIEnv* env,
        jobject self,
        jobject rasterizerState)
{
    if (rasterizerState == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetState(nullptr);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetState(
            NativeClass<ID3D11RasterizerState>::GetValuePtr(env, rasterizerState));
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_rsSetViewports
    (   JNIEnv* env,
        jobject self,
        jobjectArray viewports)
{
    if (viewports == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetViewports(0, nullptr);
    }
    else {
        auto viewportsParam = NativeClass<D3D11_VIEWPORT>::AllocArray(env, viewports);
        NativeClass<D3D11_VIEWPORT>::GetArray(env, viewports, viewportsParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->RSSetViewports(
            NativeClass<D3D11_VIEWPORT>::GetArrayLength(env, viewports), viewportsParam);

        NativeClass<D3D11_VIEWPORT>::FreeArray(viewportsParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_updateSubresource(
    JNIEnv* env,
    jobject self,
    jobject dstResource,
    jint dstSubresource,
    jobject dstBox,
    jobject srcData,
    jint srcRowPitch,
    jint srcDepthPitch)
{
    if (dstBox == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->UpdateSubresource(
            NativeClass<ID3D11Resource>::GetValuePtr(env, dstResource),
            dstSubresource,
            nullptr,
            env->GetDirectBufferAddress(srcData),
            srcRowPitch,
            srcDepthPitch);
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->UpdateSubresource(
            NativeClass<ID3D11Resource>::GetValuePtr(env, dstResource),
            dstSubresource,
            NativeClass<D3D11_BOX>::GetValuePtr(env, dstBox),
            env->GetDirectBufferAddress(srcData),
            srcRowPitch,
            srcDepthPitch);
    }
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_map(
    JNIEnv* env,
    jobject self,
    jobject resource,
    jint subresource,
    jint mapType,
    jint mapFlags)
{
    if (resource == nullptr) {
        env->Throw(IllegalArgumentException.Create(env, "resource != null"));
        return nullptr;
    }
    else {
        auto* ret = NativeClass<D3D11_MAPPED_SUBRESOURCE>::Alloc();

        auto result = NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->Map(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource),
            subresource,
            static_cast<D3D11_MAP>(mapType),
            mapFlags,
            ret);

        if (result != S_OK) {
            env->Throw(HResultException::Create(env, "ID3D11DeviceContext::Map", result));
        }

        return NativeClass<D3D11_MAPPED_SUBRESOURCE>::Create(env, ret);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_unmap(
    JNIEnv* env,
    jobject self,
    jobject resource,
    jint subresource)
{
    if (resource == nullptr) {
        env->Throw(IllegalArgumentException.Create(env, "resource != null"));
    }
    else {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->Unmap(
            NativeClass<ID3D11Resource>::GetValuePtr(env, resource), subresource);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_vsSetConstantBuffers(
    JNIEnv* env,
    jobject self,
    jint startSlot,
    jobjectArray constantBuffers)
{
    if (constantBuffers == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetConstantBuffers(startSlot, 0, nullptr);
    }
    else {
        auto buffers = NativeClass<ID3D11Buffer>::AllocArrayPtr(env, constantBuffers);
        NativeClass<ID3D11Buffer>::GetArrayPtr(env, constantBuffers, buffers);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetConstantBuffers(
            startSlot, NativeClass<ID3D11Buffer>::GetArrayLength(env, constantBuffers), buffers);

        NativeClass<ID3D11Buffer>::FreeArrayPtr(buffers);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_vsSetSamplers(
    JNIEnv* env,
    jobject self,
    jint startSlot,
    jobjectArray samplers)
{
    if (samplers == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetSamplers(startSlot, 0, nullptr);
    }
    else {
        auto samplersParam = NativeClass<ID3D11SamplerState>::AllocArrayPtr(env, samplers);
        NativeClass<ID3D11SamplerState>::GetArrayPtr(env, samplers, samplersParam);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetSamplers(
            startSlot, NativeClass<ID3D11SamplerState>::GetArrayLength(env, samplers), samplersParam);

        NativeClass<ID3D11SamplerState>::FreeArrayPtr(samplersParam);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_vsSetShader
    (   JNIEnv* env,
        jobject self,
        jobject vertexShader,
        jobjectArray classInstances)
{
    if (vertexShader == nullptr && classInstances == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShader(nullptr, nullptr, 0);
    }
    else if (vertexShader == nullptr) {
        auto instances = NativeClass<ID3D11ClassInstance>::AllocArrayPtr(env, classInstances);
        NativeClass<ID3D11ClassInstance>::GetArrayPtr(env, classInstances, instances);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShader(
            nullptr, instances, NativeClass<ID3D11VertexShader>::GetArrayLength(env, classInstances));

        NativeClass<ID3D11ClassInstance>::FreeArrayPtr(instances);
    }
    else if (classInstances == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShader(
            NativeClass<ID3D11VertexShader>::GetValuePtr(env, vertexShader), nullptr, 0);
    }
    else {
        auto instances = NativeClass<ID3D11ClassInstance>::AllocArrayPtr(env, classInstances);
        NativeClass<ID3D11ClassInstance>::GetArrayPtr(env, classInstances, instances);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShader(
            NativeClass<ID3D11VertexShader>::GetValuePtr(env, vertexShader),
            instances,
            NativeClass<ID3D11VertexShader>::GetArrayLength(env, classInstances));

        NativeClass<ID3D11ClassInstance>::FreeArrayPtr(instances);
    }
}

JNIEXPORT void JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceContext_vsSetShaderResources
    (   JNIEnv* env,
        jobject self,
        jint startSlot,
        jobjectArray shaderResourceViews)
{
    if (shaderResourceViews == nullptr) {
        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShaderResources(startSlot, 0, nullptr);
    }
    else {
        auto views = NativeClass<ID3D11ShaderResourceView>::AllocArrayPtr(env, shaderResourceViews);
        NativeClass<ID3D11ShaderResourceView>::GetArrayPtr(env, shaderResourceViews, views);

        NativeClass<ID3D11DeviceContext>::GetValuePtr(env, self)->VSSetShaderResources(
            startSlot, NativeClass<ID3D11ShaderResourceView>::GetArrayLength(env, shaderResourceViews), views);

        NativeClass<ID3D11ShaderResourceView>::FreeArrayPtr(views);
    }
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_createDeviceAndSwapChain
    (   JNIEnv* env,
        jclass clazz,
        jint driverType,
        jint flags,
        jobject swapChainDesc)
{
    ID3D11Device* device;
    ID3D11DeviceContext* context;
    IDXGISwapChain* swapChain;
    auto desc = NativeClass<DXGI_SWAP_CHAIN_DESC>::GetValuePtr(env, swapChainDesc);

    HRESULT result = D3D11CreateDeviceAndSwapChain(
        nullptr,
        static_cast<D3D_DRIVER_TYPE>(driverType),
        nullptr,
        flags,
        nullptr,
        0,
        D3D11_SDK_VERSION,
        desc,
        &swapChain,
        &device,
        nullptr,
        &context);

    if (result != S_OK) {
        env->Throw(HResultException::Create(env, "D3D11CreateDeviceAndSwapChain", result));
    }

    auto tuple = NativeClass<DeviceDeviceContextSwapChainTuple>::Alloc();
    *tuple = { device, context, swapChain };
    return NativeClass<DeviceDeviceContextSwapChainTuple>::Create(env, tuple);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceDeviceContextSwapChainTuple_getDevice
    (   JNIEnv* env,
        jobject self)
{
    return NativeClass<ID3D11Device>::Create(
        env, NativeClass<DeviceDeviceContextSwapChainTuple>::GetValuePtr(env, self)->Device);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceDeviceContextSwapChainTuple_getDeviceContext
    (   JNIEnv* env,
        jobject self)
{
    return NativeClass<ID3D11DeviceContext>::Create(
        env, NativeClass<DeviceDeviceContextSwapChainTuple>::GetValuePtr(env, self)->DeviceContext);
}

JNIEXPORT jobject JNICALL Java_dev_josh127_libwin32_D3D11_00024DeviceDeviceContextSwapChainTuple_getSwapChain
    (   JNIEnv* env,
        jobject self)
{
    return NativeClass<IDXGISwapChain>::Create(
        env, NativeClass<DeviceDeviceContextSwapChainTuple>::GetValuePtr(env, self)->SwapChain);
}