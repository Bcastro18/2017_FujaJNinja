package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author Bruno de Castro Celestino e Gabriel Nistardo Bourg
 */
public class Main extends SimpleApplication
        implements ActionListener, PhysicsCollisionListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.start();

    }
    /*  
        Menu - sair, fontes e github
     */
    private BulletAppState bulletAppState;
    private Player player;
    private PremioFerrari pf;
    private boolean left = false, right = false, reinicia = false, pausar = false;
    private Material boxMatColosion;
    private float i = 1f, j = 3f, a = 2f, b = 1f, c = 1.3f, d = 1.5f, e = 1f, f = 2.1f, g = 1f, h = 2.3f;
    public Vector3f jumpForce = new Vector3f(0, 100, 0);
    public int placar = 0;
    public ArrayList<Integer> recordes = new ArrayList<Integer>();
    AudioNode somAmbiente;

    private boolean isRunning = true;
    private final ActionListener pauseActionListener;
    private final AnalogListener pauseAnalogListener;

    public Main() {
        this.pauseAnalogListener = new AnalogListener() {
            @Override
            public void onAnalog(String name, float value, float tpf) {
                if (isRunning) {

                } else {
                    if(pausar != true)
                        Menu();
                }
            }
        };
        this.pauseActionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed,
                    float tpf) {
                if (name.equals("Pause") && !keyPressed) {
                    isRunning = !isRunning;
                    if(pausar != true)
                        Menu();
                }
            }
        };
    }

    public void InstanciaFogoCenario(){
        
    /** Uses Texture from jme3-test-data library! */
    ParticleEmitter fireEffect = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material fireMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    //fireMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
    fireEffect.setMaterial(fireMat);
    fireEffect.setImagesX(1); fireEffect.setImagesY(2); // 2x2 texture animation
    fireEffect.setEndColor( new ColorRGBA(1f, 0f, 0f, 1f) );   // red
    fireEffect.setStartColor( new ColorRGBA(1f, 1f, 0f, 0.5f) ); // yellow
    fireEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    fireEffect.setStartSize(1f);
    fireEffect.setEndSize(1f);
    fireEffect.setGravity(0f,1f,0f);
    fireEffect.setLowLife(0.5f);
    fireEffect.setHighLife(3f);
    fireEffect.getParticleInfluencer().setVelocityVariation(0.3f);
    fireEffect.setLocalTranslation(new Vector3f(11,-3,0));
    rootNode.attachChild(fireEffect);
    
    ParticleEmitter fireEffect2 = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material fireMat2 = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    fireMat2.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
    fireEffect2.setMaterial(fireMat2);
    fireEffect2.setImagesX(1); fireEffect2.setImagesY(2); // 2x2 texture animation
    fireEffect2.setEndColor( new ColorRGBA(1f, 0f, 0f, 1f) );   // red
    fireEffect2.setStartColor( new ColorRGBA(1f, 1f, 0f, 0.5f) ); // yellow
    fireEffect2.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    fireEffect2.setStartSize(1f);
    fireEffect2.setEndSize(1f);
    fireEffect2.setGravity(0f,1f,0f);
    fireEffect2.setLowLife(0.5f);
    fireEffect2.setHighLife(3f);
    fireEffect2.getParticleInfluencer().setVelocityVariation(0.3f);
    fireEffect2.setLocalTranslation(new Vector3f(-11,-3,0));
    rootNode.attachChild(fireEffect2);

    /** Explosion effect. Uses Texture from jme3-test-data library! */ 
    ParticleEmitter debrisEffect = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
    Material debrisMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    debrisMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
    debrisEffect.setMaterial(debrisMat);
    debrisEffect.setImagesX(3); debrisEffect.setImagesY(3); // 3x3 texture animation
    debrisEffect.setRotateSpeed(4);
    debrisEffect.setSelectRandomImage(true);
    debrisEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
    debrisEffect.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f));
    debrisEffect.setGravity(0f,6f,0f);
    debrisEffect.getParticleInfluencer().setVelocityVariation(.60f);
    debrisEffect.setLocalTranslation(0f, 2f, 20f);
    rootNode.attachChild(debrisEffect);
    debrisEffect.emitAllParticles();

        
        
        
        
    }
    
    public void Menu(){
        List<String> optionList = new ArrayList<String>();
        optionList.add("0");
        optionList.add("1");
        optionList.add("2");
        optionList.add("3");
        optionList.add("4");
        optionList.add("5");
        optionList.add("6");
        optionList.add("7");
        optionList.add("8");
        optionList.add("9");
        Object[] options = optionList.toArray();
        int value;
        value = JOptionPane.showOptionDialog(
                null,
                "Selecione um dos itens:\n "
                        + "0. Sair\n"
                        + " 1. Novo Jogo\n "
                        + "2. Melhores\n "
                        + "3. Ajuda\n "
                        + "4. Sobre\n"
                        + " 5. GitHub\n"
                        + " 6. Referências/Fontes\n"
                        + " 7. Áudio e Vídeo\n"
                        + " 8. Voltar ao jogo\n"
                        + " 9. Dificuldade\n",
                "Opção:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                optionList.get(0));
        
        if (value == 1) {
            System.out.println(1);
            pausar = false;
            setPausar(false);
            reinicia = true;
        }
        if(value == 2){
            CalculaRecordes(0);
            Object[] rc = recordes.toArray();
            JOptionPane.showOptionDialog(
                    null,
                    "Melhores:\n ",
                    "",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    rc, recordes.get(0));
            
        }
        if(value == 3){
            JOptionPane.showMessageDialog(null,
                  "Teclas de Comando:"
                + "W - Pular\n"
                + "E - Andar para a esquerda\n"
                + "D - Andar para a direita\n"
                + "S - Bloquear\n"
                + "P - Pausar\n"
                + "Objetivo:"
                + "Chegar até o último degrau com a menor quantidade possível de saltos."
                + "No fim haverá um prêmio, que é o carro que o ninja pode utilizar para escapar da cidade.");   
            Menu();
        }
        if(value == 4){
            JOptionPane.showMessageDialog(null,
                  "Autores: \n"
                   + "Bruno de Castro Celestino - 140576\n"
                   + "Gabriel Nistardo Bourg - 140839"
                   + "\n\nEstudantes de Engenharia da Computação - Facens - Sorocaba-SP");
            Menu();
            
        }
        if(value == 5){
            JOptionPane.showMessageDialog(null,
                  "Github.com/BCastro18");
            Menu();
            
        }
        if(value == 6){
            JOptionPane.showMessageDialog(null,
                  "Referências:\n"
                + "Todas as imagens e áudios utilizados no projeto são do JMonkey e utilizados para teste."
                + "Todos com os devidos direitos autorais permitidos.");
            Menu();
        }
        if(value == 7){
            List<String> som = new ArrayList<String>();
            som.add("Sim");
            som.add("Não");
            Object[] somOp = som.toArray();
            int op;
            op = JOptionPane.showOptionDialog(
                    null,
                    "Deseja som?\n ",
                    "Opção:",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    somOp,
                    som.get(0));
            if(op == 0){
                somAmbiente.play();
            }
            if(op == 1){
                somAmbiente.stop();
            }
        }
        if(value == 8){
            if(pausar == true){
                pausar = false;
                setPausar(false);
            }
        }
        
        if(value == 0){
            System.exit(0);
        }
        
        if(value == 9){
            List<String> dif = new ArrayList<String>();
            dif.add("Easy");
            dif.add("Medium");
            dif.add("Hard");
            Object[] Ops = dif.toArray();
            int opDif;
            opDif = JOptionPane.showOptionDialog(
                    null,
                    "Qual dificuldade?\n ",
                    "Opções:",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    Ops,
                    dif.get(0));
            if(opDif == 0){
                i = 1f; j = 3f; a = 2f; b = 1f; c = 1.3f; d = 1.5f; e = 1f; f = 2.1f; g = 1f; h = 2.3f;
            }
            if(opDif == 1){
                i = 2f; j = 3f; a = 3f; b = 2f; c = 0.3f; d = 2.5f; e = 2f; f = 3.1f; g = 2f; h = 3.3f;
            }
            if(opDif == 2){
                i = 2.3f; j = 3.3f; a = 3.3f; b = 2.3f; c = 0.6f; d = 2.8f; e = 2.3f; f = 3.5f; g = 2.3f; h = 3.7f;
            }
            
            
        }
    }
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        
        createLigth();
        createCity();
        createPlayer();
        create10Cubes();
        initKeys();
        InstanciaFogoCenario();
        
        somAmbiente = new AudioNode(assetManager,"Sound/Environment/Nature.ogg", DataType.Stream); 
        somAmbiente.setPositional(false);
        somAmbiente.setLooping(true);
        somAmbiente.setVolume(5);
        somAmbiente.play();
                
        boxMatColosion = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        boxMatColosion.setBoolean("UseMaterialColors", true);
        boxMatColosion.setColor("Ambient", ColorRGBA.Red);
        boxMatColosion.setColor("Diffuse", ColorRGBA.Red);
        bulletAppState.setDebugEnabled(false);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    private void premioFerrari() {
        pf = new PremioFerrari("premioFerrari", assetManager, inputManager, bulletAppState, cam);
        rootNode.attachChild(pf);
        flyCam.setEnabled(false);
    }

    private void createPlayer() {
        player = new Player("player", assetManager, inputManager, bulletAppState, cam);
        rootNode.attachChild(player);
        flyCam.setEnabled(false);
    }

    private void createLigth() {
        DirectionalLight l1 = new DirectionalLight();
        l1.setDirection(new Vector3f(1, -0.7f, 0));
        rootNode.addLight(l1);

        DirectionalLight l2 = new DirectionalLight();
        l2.setDirection(new Vector3f(-1, 0, 0));
        rootNode.addLight(l2);

        DirectionalLight l3 = new DirectionalLight();
        l3.setDirection(new Vector3f(0, 0, -1.0f));
        rootNode.addLight(l3);

        DirectionalLight l4 = new DirectionalLight();
        l4.setDirection(new Vector3f(0, 0, 1.0f));
        rootNode.addLight(l4);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }

    private void initKeys() {

        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(pauseActionListener, new String[]{"Pause"});
        inputManager.addListener(this, "CharForward", "CharBackward");
        inputManager.addListener(this, "CharLeft", "CharRight");

    }

    private void createCity() {
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial scene = assetManager.loadModel("main.scene");
        scene.setLocalTranslation(0, -5.2f, 0);
        rootNode.attachChild(scene);

        RigidBodyControl cityPhysicsNode = new RigidBodyControl(CollisionShapeFactory.createMeshShape(scene), 0);
        scene.addControl(cityPhysicsNode);
        bulletAppState.getPhysicsSpace().add(cityPhysicsNode);
    }

    private void create10Cubes() {
        for (int i = 1; i < 11; i++) {
            createCubo(1, i);
        }
    }

    private void createCubo(float size, int dir) {

        Box boxMesh = new Box(2 * size, size / 3, 2 * size);
        Geometry boxGeo = new Geometry("Box_" + dir, boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        boxMat.setBoolean("UseMaterialColors", true);
        boxMat.setColor("Ambient", ColorRGBA.Green);
        boxMat.setColor("Diffuse", ColorRGBA.Green);
        boxGeo.setMaterial(boxMat);

        if (dir > 1) {
            dir *= 4;
        }

        if (dir == 1) {
            boxGeo.setLocalTranslation(-5, dir, 0);
        } else if (dir == 40) {
            boxGeo.setLocalTranslation((int) (10 * Math.random()), dir, 0);
            premioFerrari();
        } else if (dir % 2 == 0) {
            boxGeo.setLocalTranslation((int) (-10 * Math.random()), dir, 0);
        } else {
            boxGeo.setLocalTranslation((int) (10 * Math.random()), dir, 0);
        }
        rootNode.attachChild(boxGeo);

        RigidBodyControl boxPhysicsNode = new RigidBodyControl(1);
        boxPhysicsNode.setMass(0);
        boxGeo.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!pausar) {
            for (Spatial obj : rootNode.getChildren()) {
                if (obj.getName().contains("Box")) {
                    Vector3f loc;
                    switch (obj.getName().split("_")[1]) {
                        case "1":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x < -10 || loc.x > 10) {
                                i *= -1;
                            }
                            loc.x += (i * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "2":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                j *= -1;
                            }
                            loc.x += (j * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "3":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                a *= -1;
                            }
                            loc.x += (a * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "4":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                b *= -1;
                            }
                            loc.x += (b * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "5":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                c *= -1;
                            }
                            loc.x += (c * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "6":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                d *= -1;
                            }
                            loc.x += (d * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "7":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                e *= -1;
                            }
                            loc.x += (e * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "8":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                f *= -1;
                            }
                            loc.x += (f * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "9":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                g *= -1;
                            }
                            loc.x += (g * tpf);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                        case "10":
                            loc = obj.getControl(RigidBodyControl.class).getPhysicsLocation();
                            if (loc.x <= -10 || loc.x >= 10) {
                                h *= -1;
                            }
                            loc.x += (tpf * h);
                            obj.getControl(RigidBodyControl.class).setPhysicsLocation(loc);
                            break;
                    }
                }
            }
        }

        if (reinicia) {
            player.setPhysicsCharacter(new Vector3f(0, 0, 0));
            CalculaRecordes(placar);
            placar = 0;
            reinicia = false;
        }
        player.upDateKeys(tpf, left, right);

        if (!isRunning) {
            guiNode.detachAllChildren();
            guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText helloText = new BitmapText(guiFont, false);
            helloText.setSize(guiFont.getCharSet().getRenderedSize());
            helloText.setColor(ColorRGBA.Black);
            helloText.setText("PAUSE");
            helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
            guiNode.attachChild(helloText);
            setPausar(true);
        } else {
            guiNode.detachAllChildren();
            setPausar(false);
            guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText text = new BitmapText(guiFont, false);
            text.setSize(guiFont.getCharSet().getRenderedSize());
            text.setColor(ColorRGBA.Black);
            text.setText("Placar: " + placar);
            text.setLocalTranslation(30, text.getLineHeight() + 445, 0);
            guiNode.attachChild(text);
        }
    }

    public void CalculaRecordes(int plc){

        recordes.add(plc);
        Collections.sort(recordes);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    //Usado para operações do tipo on/off 
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        //somente para cima
        if (name.equals("CharForward") && !keyPressed) {
            if (!player.animChannel.getAnimationName().equals("HighJump")) {
                placar += 1;
                player.animChannel.setAnim("HighJump", 0.5f);
                player.physicsCharac.setJumpForce(jumpForce);
                player.physicsCharac.jump();
                player.animChannel.setLoopMode(LoopMode.DontLoop);

            } else {
                placar += 1;
                player.animChannel.setAnim("HighJump", 0.5f);
                player.physicsCharac.setJumpForce(jumpForce);
                player.physicsCharac.jump();
                player.animChannel.setLoopMode(LoopMode.DontLoop);
            }
        }
        if (name.equals("CharBackward") && !keyPressed) {
            if (!player.animChannel.getAnimationName().equals("Block")) {
                player.animChannel.setAnim("Block", 0.5f);
                player.animChannel.setLoopMode(LoopMode.DontLoop);

            } else {
                player.animChannel.setAnim("Block", 0.5f);
                player.animChannel.setLoopMode(LoopMode.DontLoop);
            }
        }
        switch (name) {
            case "CharLeft":
                if (keyPressed) {
                    left = true;
                } else {
                    left = false;
                }
                break;
            case "CharRight":
                if (keyPressed) {
                    right = true;
                } else {
                    right = false;
                }
                break;
        }
    }
    @Override
    public void collision(PhysicsCollisionEvent event) {

        if (event.getNodeA().getName().equals("player") && event.getNodeB().getName().contains("Box")) {
            event.getNodeB().setMaterial(boxMatColosion);
        } else if (event.getNodeA().getName().contains("Box") && event.getNodeB().getName().equals("player")) {
            event.getNodeA().setMaterial(boxMatColosion);
        }

        if (event.getNodeA().getName().equals("player") && event.getNodeB().getName().contains("premioFerrari")) {
            reinicia = true;
            placar = 0;
        } else if (event.getNodeA().getName().contains("premioFerrari") && event.getNodeB().getName().equals("player")) {
            reinicia = true;
            placar = 0;
        }

    }

    public void setPausar(boolean y) {
        if (y) {
            pausar = true;
            inputManager.removeListener(this);
            inputManager.removeListener(this);
        }
        if (!y) {
            pausar = false;
            inputManager.addListener(this, "CharForward", "CharBackward");
            inputManager.addListener(this, "CharLeft", "CharRight");
        }
    }
}
