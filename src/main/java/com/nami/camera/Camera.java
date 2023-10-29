package com.nami.camera;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    //attributes
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f front = new Vector3f(0, 0, -1);
    private Vector3f up = new Vector3f(0, 1, 0);
    private Vector3f right = new Vector3f(1, 0, 0);
    private Vector3f worldUp = new Vector3f(0, 1, 0);

    private Matrix4f projection;

    //options
    private float fovRAD;
    private float aspectRatio;
    private float zNear, zFar;

    //euler's angles
    private float yaw, pitch;

    public Camera(float fov, float aspectRatio, float zNear, float zFar) {
        this.fovRAD = (float) Math.toRadians(fov);
        this.aspectRatio = aspectRatio;

        this.zNear = zNear;
        this.zFar = zFar;

        updateProjection();
    }

    public Matrix4f getViewMatrix() {
        Matrix4f matrix = new Matrix4f();
        Vector3f vec = new Vector3f();

        position.add(front, vec);
        matrix.lookAt(position, vec, up);

        return matrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

    public void move(CameraMovement dir, float speed, float deltaTime) {
        float velocity = speed * deltaTime;

        Vector3f vec = new Vector3f();
        if (dir == CameraMovement.FORWARD)
            front.mul(velocity, vec);
        if (dir == CameraMovement.BACKWARD)
            front.mul(-velocity, vec);
        if (dir == CameraMovement.LEFT)
            right.mul(-velocity, vec);
        if (dir == CameraMovement.RIGHT)
            right.mul(velocity, vec);
        if (dir == CameraMovement.UP)
            worldUp.mul(velocity, vec);
        if (dir == CameraMovement.DOWN)
            worldUp.mul(-velocity, vec);
        position.add(vec);
    }

    public void mouse(float xOffset, float yOffset, boolean constrainPitch, float sensivity) {
        xOffset *= sensivity;
        yOffset *= sensivity;

        yaw += xOffset;
        pitch += yOffset;

        if (constrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        Vector3f nFront = new Vector3f();
        nFront.x = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        nFront.y = Math.sin(Math.toRadians(pitch));
        nFront.z = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        nFront.normalize(front);

        Vector3f nRight = new Vector3f();
        front.cross(worldUp, nRight);
        nRight.normalize(right);

        Vector3f nUp = new Vector3f();
        right.cross(front, nUp);
        nUp.normalize(up);
    }

    private void updateProjection() {
        projection = new Matrix4f();
        projection.perspective(fovRAD, aspectRatio, zNear, zFar);
    }

    public enum CameraMovement {
        FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN;
    }

    public void setFovDEG(float fov) {
        this.fovRAD = (float) Math.toRadians(fov);
        updateProjection();
    }

    public float getFovDEG() {
        return (float) Math.toDegrees(fovRAD);
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        updateProjection();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getPosition() {
        return position;
    }

}
