/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Bruno
 */
public class PremioFerrari extends Node{
    
    private final BetterCharacterControl physicsCharac;
    
    public PremioFerrari (String name, AssetManager assetManager, InputManager inputManager , BulletAppState bulletAppState, Camera cam) {
        super(name);

        Node ferrari = (Node) assetManager.loadModel("Models/Ferrari/Car.mesh.xml");
        ferrari.scale(1f, 1f, 1f);
        ferrari.setLocalTranslation(0, 48, 0);
        attachChild(ferrari);
        
        RigidBodyControl boxPhysicsNode = new RigidBodyControl(1);
        boxPhysicsNode.setMass(0);   
        physicsCharac = new BetterCharacterControl(1, 2.5f, 16f);
        addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
   }
    
    public void setPhysicsCharacter(Vector3f pos) {
        physicsCharac.warp(pos);
    }
    
    
}
