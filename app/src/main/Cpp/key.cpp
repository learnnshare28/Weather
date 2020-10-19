#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring,JNICALL
java_com_arjun_weather_MainActivity_APIKeyLibrary_getAPIKeygetAPIKey(JNIEnv* env, jobject /* this */) {
    std::string api_key = "API_KEY_YAHA_HUNCHA_HAI";
    return env->NewStringUTF(api_key.c_str());
}