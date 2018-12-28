package net.survival.client;

import org.joml.Vector3d;

import net.survival.client.input.Keyboard;
import net.survival.client.input.Mouse;
import survival.input.Key;

class UserController
{
    public FpvCamera camera;

    public UserController() {
        camera = new FpvCamera(new Vector3d(0.0, 72.0f, 0.0), 0.0, -1.0);
    }

    public void tick(double elapsedTime) {
        double movementSpeed = elapsedTime * 6.0;
        double verticalSpeed = elapsedTime * 4.0;

        if (Keyboard.isKeyDown(Key.LEFT_CONTROL)) {
            movementSpeed *= 3.0;
            verticalSpeed *= 3.0;
        }
        
        if (Keyboard.isKeyDown(Key.S))
            camera.moveXZ(Math.PI, movementSpeed);
        else if (Keyboard.isKeyDown(Key.W))
            camera.moveXZ(0.0, movementSpeed);
        
        if (Keyboard.isKeyDown(Key.A))
            camera.moveXZ(-Math.PI / 2.0, movementSpeed);
        else if (Keyboard.isKeyDown(Key.D))
            camera.moveXZ(Math.PI / 2.0, movementSpeed);
        
        if (Keyboard.isKeyDown(Key.SPACE))
            camera.moveY(verticalSpeed);
        else if (Keyboard.isKeyDown(Key.LEFT_SHIFT))
            camera.moveY(-verticalSpeed);
        
        double cursorDX = Mouse.getDeltaX();
        double cursorDY = Mouse.getDeltaY();
        camera.rotate(-cursorDX / 64.0, -cursorDY / 64.0);
    }
}