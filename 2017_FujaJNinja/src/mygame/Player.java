/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
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
public class Player extends Node{
    
    
    public final BetterCharacterControl physicsCharac;
    private final AnimControl animControl;
    public final AnimChannel animChannel;
    private Vector3f walkDirec = new Vector3f(0, 0, 0);
    private Vector3f viewDirec = new Vector3f(0, 0, 0);

//Death1
//HighJump
//SideKick
//Backflip
//Block
//Climb
//Crouch
    
    
    public Player (String name, AssetManager assetManager, InputManager inputManager , BulletAppState bulletAppState, Camera cam) {
        super(name);

        Node ninja = (Node) assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.03f, 0.03f, 0.03f);
        ninja.setLocalTranslation(0, 0, 0);
        attachChild(ninja);
        
        physicsCharac = new BetterCharacterControl(1, 5f, 8f);
        addControl(physicsCharac);
        bulletAppState.getPhysicsSpace().add(physicsCharac);
        animControl = ninja.getControl(AnimControl.class);
        animChannel = animControl.createChannel();
        
        animChannel.setAnim("Block");
        animChannel.setLoopMode(LoopMode.DontLoop);
        
        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 8,-25));
        camNode.lookAt(this.getLocalTranslation(), Vector3f.UNIT_Y);
        this.attachChild(camNode);
   }
    
    
    public Vector3f getWalkDirec() {
        return walkDirec;
    }
    public void setWalkDirec(Vector3f walkDirection) {
        this.walkDirec = walkDirection;
    }
    public Vector3f getViewDirec() {
        return viewDirec;
    }
    public void setViewDirec(Vector3f viewDirection) {
        this.viewDirec = viewDirection;
    }        
    void upDateKeys(float tpf, boolean left, boolean right){
  
        Vector3f camDir  = getWorldRotation().mult(Vector3f.UNIT_X);
            walkDirec.set(0, 0, 0);

            if (left) {
                if(getWorldTranslation().x < 5)
                    walkDirec.addLocal(camDir.mult(5));
                    //System.out.println(getWorldTranslation());
            } else if (right) {
                if(getWorldTranslation().x > -5)
                    walkDirec.addLocal(camDir.mult(-5));
                    //System.out.println(getWorldTranslation());
            }
            physicsCharac.setWalkDirection(walkDirec);

    }
    public void setPhysicsCharacter(Vector3f pos) {
        physicsCharac.warp(pos);
    }

}
