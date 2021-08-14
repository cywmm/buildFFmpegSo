#include <jni.h>
#include <string>
#include <unistd.h>
#include "media/player/def_player/player.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>
#include <libavcodec/jni.h>
JNIEXPORT jstring JNICALL
Java_com_ming_buildffmpegso_MainActivity_getFFmpegInfo(JNIEnv *env, jobject thiz) {
    char info[40000] = {0};
    AVCodec *c_temp = av_codec_next(NULL);
    while (c_temp != NULL) {
        if (c_temp->decode != NULL) {
            switch (c_temp->type) {
                case AVMEDIA_TYPE_VIDEO:
                    sprintf(info, "%s(video):", info);
                    break;
                case AVMEDIA_TYPE_AUDIO:
                    sprintf(info, "%s(audio):", info);
                    break;
                default:
                    sprintf(info, "%s(other):", info);
                    break;
            }
            sprintf(info, "%s[%10s]\n", info, c_temp->name);
        } else {
            sprintf(info, "%sencode:", info);
        }
        c_temp = c_temp->next;
    }
    return env->NewStringUTF(info);
}


extern "C" {
#include <libavfilter/avfilter.h>
JNIEXPORT jstring JNICALL
Java_com_ming_buildffmpegso_MainActivity_getFFmpegFilterInfo(JNIEnv *env, jobject thiz) {
    char info[40000] = {0};
    avfilter_register_all();

    AVFilter *f_temp = (AVFilter *) avfilter_next(NULL);
    while (f_temp != NULL) {
        sprintf(info, "%s%s\n", info, f_temp->name);
        f_temp = f_temp->next;
    }
    return env->NewStringUTF(info);
}
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_ming_buildffmpegso_MainActivity_createPlayer(JNIEnv *env, jobject thiz, jstring path,
                                                      jobject surface) {
    Player *player = new Player(env, path, surface);
    return (jint) player;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ming_buildffmpegso_MainActivity_play(JNIEnv *env, jobject thiz, jint player) {
    Player *p = (Player *) player;
    p->play();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ming_buildffmpegso_MainActivity_pause(JNIEnv *env, jobject thiz, jint player) {
    Player *p = (Player *) player;
    p->pause();
}
}