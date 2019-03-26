#ifndef JOSH127_LIBWIN32_HH
#define JOSH127_LIBWIN32_HH
#include <jni.h>
#include <Windows.h>
#include <d3d11.h>

#define global_var static
#define local_persist static
#define internal_func static

using uint8 = unsigned char;
using uint16 = unsigned short;
using uint32 = unsigned int;
using uint64 = unsigned long long;
using int8 = char;
using int16 = short;
using int32 = int;
using int64 = long long;
using intptr = int64*;

[[nodiscard]] inline int8 ToSigned(uint8 value) { return *((int8*) &value); }
[[nodiscard]] inline uint8 ToUnsigned(int8 value) { return *((uint8*) &value); }
[[nodiscard]] inline int16 ToSigned(uint16 value) { return *((int16*) &value); }
[[nodiscard]] inline uint16 ToUnsigned(int16 value) { return *((uint16*) &value); }
[[nodiscard]] inline int32 ToSigned(uint32 value) { return *((int32*) &value); }
[[nodiscard]] inline uint32 ToUnsigned(int32 value) { return *((uint32*) &value); }
[[nodiscard]] inline int64 ToSigned(uint64 value) { return *((int64*) &value); }
[[nodiscard]] inline uint64 ToUnsigned(int64 value) { return *((uint64*) &value); }

#define HandleJNIExceptionVoid(env) { \
    if (env->ExceptionCheck()) { \
        env->Throw(env->ExceptionOccurred()); \
        return; \
    } \
}

#define HandleJNIExceptionObject(env) { \
    if (env->ExceptionCheck()) { \
        env->Throw(env->ExceptionOccurred()); \
        return nullptr; \
    } \
}

[[nodiscard]] inline jobject CreateDirectByteBuffer(JNIEnv* env, void* address, int32 capacity) {
    jobject ret = env->NewDirectByteBuffer(address, capacity);
    HandleJNIExceptionObject(env);
    return ret;
}

class NativePtr {
    static jclass clazz;
    static jfieldID addressID;
    static jmethodID constructorID;

public:
    static inline void Init(JNIEnv* env, const char* className)
    {
        jclass local = env->FindClass(className);
        HandleJNIExceptionVoid(env);
        clazz = reinterpret_cast<jclass>(env->NewGlobalRef(local));
        env->DeleteLocalRef(local);
        addressID = env->GetFieldID(clazz, "address", "J");
        HandleJNIExceptionVoid(env);
        constructorID = env->GetMethodID(clazz, "<init>", "(J)V");
        HandleJNIExceptionVoid(env);
    }

    template<class T>
    static inline T GetValue(JNIEnv* env, jobject obj) {
        auto ret = reinterpret_cast<T>(env->GetLongField(obj, addressID));
        return ret;
    }

    template<class T>
    static inline T* GetValuePtr(JNIEnv* env, jobject obj) {
        auto ret = reinterpret_cast<T*>(env->GetLongField(obj, addressID));
        return ret;
    }

    template<class T>
    static inline jobject Create(JNIEnv* env, T* obj) {
        return env->NewObject(clazz, constructorID, reinterpret_cast<jlong>(obj));
    }
};

template<class T>
class NativeClass {
protected:
    static jclass clazz;
    static jfieldID addressID;
    static jmethodID constructorID;

public:
    static inline void Init(JNIEnv* env, const char* className) {
        jclass local = env->FindClass(className);
        HandleJNIExceptionVoid(env);
        clazz = reinterpret_cast<jclass>(env->NewGlobalRef(local));
        env->DeleteLocalRef(local);
        addressID = env->GetFieldID(clazz, "address", "J");
        HandleJNIExceptionVoid(env);
        constructorID = env->GetMethodID(clazz, "<init>", "(J)V");
        HandleJNIExceptionVoid(env);
    }

    static inline T* GetValuePtr(JNIEnv* env, jobject obj) {
        return reinterpret_cast<T*>(env->GetLongField(obj, addressID));
    }

    static inline int32 GetArrayLength(JNIEnv* env, jobjectArray array) {
        return env->GetArrayLength(array);
    }

    static inline void GetArray(JNIEnv* env, jobjectArray array, T* ret) {
        auto size = env->GetArrayLength(array);

        for (auto i = 0; i < size; i++)
            ret[i] = *GetValuePtr(env, env->GetObjectArrayElement(array, i));
    }

    static inline void GetArrayPtr(JNIEnv* env, jobjectArray array, T** ret) {
        auto size = env->GetArrayLength(array);

        for (auto i = 0; i < size; i++) {
            auto element = env->GetObjectArrayElement(array, i);
            ret[i] = element == nullptr ? nullptr : GetValuePtr(env, element);
        }
    }

    static inline jobject Create(JNIEnv* env, T* obj) {
        auto ret = env->NewObject(clazz, constructorID, reinterpret_cast<jlong>(obj));
        HandleJNIExceptionObject(env);
        return ret;
    }

    static inline T* Alloc() {
        // Dunno why GlobalAlloc didn't work. Ah well. >:/
        return reinterpret_cast<T*>(VirtualAlloc(0, sizeof(T*), MEM_COMMIT, PAGE_READWRITE));
    }

    static inline T* AllocArray(int32 length) {
        return reinterpret_cast<T*>(VirtualAlloc(0, sizeof(T), MEM_COMMIT, PAGE_READWRITE));
    }

    static inline T* AllocArray(JNIEnv* env, jobjectArray array) {
        return AllocArray(env->GetArrayLength(array));
    }

    static inline void FreeArray(T* array) {
        VirtualFree(reinterpret_cast<LPVOID>(array), 0, MEM_RELEASE);
    }

    static inline T** AllocArrayPtr(int32 length) {
        return reinterpret_cast<T**>(VirtualAlloc(0, length * sizeof(T*), MEM_COMMIT, PAGE_READWRITE));
    }

    static inline T** AllocArrayPtr(JNIEnv* env, jobjectArray array) {
        return AllocArrayPtr(env->GetArrayLength(array));
    }

    static inline void FreeArrayPtr(T** array) {
        VirtualFree(reinterpret_cast<LPVOID>(array), 0, MEM_RELEASE);
    }
};

template<class T>
jclass NativeClass<T>::clazz = nullptr;

template<class T>
jfieldID NativeClass<T>::addressID = nullptr;

template<class T>
jmethodID NativeClass<T>::constructorID = nullptr;

class HResultException
{
    static jclass clazz;
    static jmethodID constructorID;

public:
    static inline void Init(JNIEnv* env, const char* className) {
        jclass local = env->FindClass(className);
        HandleJNIExceptionVoid(env);
        clazz = reinterpret_cast<jclass>(env->NewGlobalRef(local));
        env->DeleteLocalRef(local);
        constructorID = env->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
        HandleJNIExceptionVoid(env);
    }

    static inline jthrowable Create(JNIEnv* env, const char* function, int32 result) {
        auto ret = reinterpret_cast<jthrowable>(env->NewObject(clazz, constructorID,
            env->NewStringUTF(function), result));
        HandleJNIExceptionObject(env);
        return ret;
    }
};

class JavaException {
    jclass clazz;
    jmethodID constructorID;

public:
    inline void Init(JNIEnv* env, const char* className) {
        jclass local = env->FindClass(className);
        HandleJNIExceptionVoid(env);
        clazz = reinterpret_cast<jclass>(env->NewGlobalRef(local));
        env->DeleteLocalRef(local);
        constructorID = env->GetMethodID(clazz, "<init>", "(Ljava/lang/String;)V");
        HandleJNIExceptionVoid(env);
    }

    inline jthrowable Create(JNIEnv* env, const char* message) {
        auto ret = reinterpret_cast<jthrowable>(env->NewObject(clazz, constructorID, env->NewStringUTF(message)));
        HandleJNIExceptionObject(env);
        return ret;
    }
};

extern JavaException IllegalArgumentException;

struct DeviceDeviceContextSwapChainTuple {
    ID3D11Device* Device;
    ID3D11DeviceContext* DeviceContext;
    IDXGISwapChain* SwapChain;

    DeviceDeviceContextSwapChainTuple(ID3D11Device* device, ID3D11DeviceContext* context, IDXGISwapChain* swapChain) :
        Device(device), DeviceContext(context), SwapChain(swapChain)
    {}
};

#endif