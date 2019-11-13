package net.survival.graphics;

import org.joml.Matrix4f;

interface Camera
{
    void getViewMatrix(Matrix4f dest);

    void getProjectionMatrix(Matrix4f dest);
}