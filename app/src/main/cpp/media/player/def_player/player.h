//
// Created by cxp on 2020-03-18.
//

#ifndef LEARNINGVIDEO_PLAYER_H
#define LEARNINGVIDEO_PLAYER_H


#include "../../decoder/video/v_decoder.h"

class Player {
private:
    VideoDecoder *m_v_decoder;
    VideoRender *m_v_render;
public:
    Player(JNIEnv *jniEnv, jstring path, jobject surface);
    ~Player();

    void play();
    void pause();
};


#endif //LEARNINGVIDEO_PLAYER_H
