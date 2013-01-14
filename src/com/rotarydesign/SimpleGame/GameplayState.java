package com.rotarydesign.SimpleGame;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;
import java.io.*;
import java.util.*;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


	

public class GameplayState extends BasicGameState{
	
	private enum STATES {
        START_GAME_STATE, INITIALIZE, MOVING_PIECE_STATE, PLAYING,
        PAUSE_GAME_STATE, HIGHSCORE_STATE, GAME_OVER_STATE
    }
	
	public static STATES currentState = null;
	
	@Override
    public void enter(GameContainer gc, StateBasedGame sb) throws SlickException
    {
        super.enter(gc, sb);
        gc.setAlwaysRender(true);
        
        currentState = STATES.START_GAME_STATE;
        
 
        //score = 0;
    }
	
	int stateID = 1;
	 GameplayState( int stateID ) 
	    {
	       this.stateID = stateID;
	    }
	 
	 @Override
	    public int getID() {
	        return stateID;
	    }
	 
	public void reset() {

	}
	
	Image land = null;
	Image plane = null;
	Image shieldIcon;
	Bar healthBar = null;
	Bar chargeBar = null;
	Polygon planePoly;
	Ellipse planeShield;
	Image bullet = null;
	Image shield = null;
	Sound laser = null;
	Sound smoke = null;
	ArrayList <Bullet> bullets = new ArrayList <Bullet>();
	static ArrayList <Powerup> powerups = new ArrayList <Powerup>();
	static ArrayList <Bullet> enemyBullets = new ArrayList <Bullet>();
	ArrayList <Enemy> enemies = new ArrayList <Enemy>();
	ArrayList <Background> backgrounds = new ArrayList <Background>();
	public Music music = null;
	int x = 0;
	int y = 100;
	float scale = 1.0f;
	float lastFire = 0 ;
	float lastSpawn = 0;
	int spawnInterval = 0;
	int landX = 0;
	int score = 0;
	int bulletRecharge = 0;
	int bulletPassing = 0;
	static boolean playing = true;
	static int health = 100;
	float hip = 0 ;
	float endTimer = 0;
	boolean cooldown = false;
	boolean powerupShield = false;
	static boolean needReset = false;
	int enemiesPassed = 0;
	int playerBulletDamage = 20;
	int timer;
	static float deltaTime = 0;
	
	 
 

 
	public void initialize()
	{
		
        healthBar.bars = 5;
     	//chargeBar.bars = 0;
     	enemies.clear();
     	enemyBullets.clear();
     	backgrounds.clear();
     	powerups.clear();
     	
     	bullets.clear();
     	x = 0;
     	y = 100;
     	scale = 1.0f;
     	lastFire = 0 ;
     	lastSpawn = 0;
     	spawnInterval = 0;
     	landX = 0;
     	score = 0;
     	bulletRecharge = 0;
     	bulletPassing = 0;
     	playing = true;
     	health = 100;
     	hip = 0 ;
     	timer = 0;
     	endTimer = 0;
     	cooldown = false;
     	powerupShield = false;
     	music.loop(1f,.3f);
     	needReset = false;
     	enemiesPassed = 0;
     	playerBulletDamage = 20;
	}
    public void init(GameContainer gc, StateBasedGame sbg) 
			throws SlickException {
    	
    	File filename = new File("C:/levels/level01.txt");
    	//FileReader fw = new FileReader(filename);
    	/*FileReader reader;
    	//Scanner in = new Scanner(new FileReader("levels/level01.txt"));
        while (in.hasNextLine()) // NOT at the end of the stream, more input is available
        {
            String thisLine = in.nextLine(); // Get an entire line
            for (int index=0; index < thisLine.length(); index++)
            {
                char ch = thisLine.charAt(index);
                System.out.print(ch);
            }
            System.out.println();
        }
        in.close();*/

    	//land = new Image("assets/background_space.png");
    	plane = new Image("assets/ship.png");
    	
		planePoly = new Polygon(new float[]{
				x,y,
				x+113,y,
				x+113,y+34,
				x+38,y+35,
				x,y+38
		});
		
		shield = new Image("assets/shield.png");
		
		planeShield = new Ellipse(x+56.5f,y+19f,90f,44f);
		
		healthBar = new Bar(400,5);
		chargeBar = new Bar(600,5);
    	
    	laser = new Sound("assets/laser.wav");
    	bullet = new Image("assets/bullet.jpg");
    	//bulletGeneration = new BulletGeneration();
    	music = new Music("assets/bgmusic.wav");
    	smoke = new Sound("assets/smoke.wav");
    	
    	backgrounds.add(new Background(0,0,1));
    	backgrounds.add(new Background(400,0,2));
    	backgrounds.add(new Background(800,0,3));
    	backgrounds.add(new Background(1200,0,4));    	
    	backgrounds.add(new Background(1600,0,5));
    	backgrounds.add(new Background(2000,0,6));
    	backgrounds.add(new Background(2400,0,7));
    	backgrounds.add(new Background(2800,0,8));
    	
    	
 
    }
 
   
    public void update(GameContainer gc, StateBasedGame sbg, int delta) 
			throws SlickException     
    {
    	
    	if(needReset)
    	{
    		currentState = STATES.START_GAME_STATE;
    		needReset = false;
    	}
    	 switch(currentState)
         {
             case START_GAME_STATE:
                currentState = STATES.INITIALIZE;
                
             case INITIALIZE:
                 
                 
                 //initialize();
                 initialize();
                 init(gc, sbg);
                  currentState = STATES.PLAYING;
                 break;
             case PLAYING:
               //  updatePiece( gc, sb, delta);
                 break;
             //case HIGHSCORE_STATE:
                // break;
             case PAUSE_GAME_STATE:
            	 sbg.enterState(SimpleGame.PAUSEDSTATE);
                 break;
             case GAME_OVER_STATE:
            	// init(gc, sbg);
  
                // Highscores.getInstance().addScore(score);
            	 currentState = STATES.START_GAME_STATE;
                // sbg.enterState(SimpleGame.MAINMENUSTATE);
                 break;
         }

    	Input input = gc.getInput();
    	//font
    	
    	
    	//collision	start----------------
    	
    	//bullet and enemy
    	for(int i = 0; i < bullets.size(); i++)
    	{
    		for(int j=0; j< enemies.size();j++)
    		{
    			
    		if(bullets.get(i).bulletPoly.intersects(enemies.get(j).enemyPoly))
    				{
    			score+=15;
    			if(enemies.get(j).health - playerBulletDamage > 0)
    			{
    			enemies.get(j).health -= playerBulletDamage;
    			bullets.remove(bullets.get(i));
    			break;
    			}
    			else
    			{
    			bullets.remove(bullets.get(i));
    			enemies.remove(enemies.get(j));
    			}
    			break;
    				}
    		
    		
    		
    		}
    		
    	}
    	//bullets (both)
    	
    	for(int i = 0; i < bullets.size(); i++)
    	{
    		for(int j=0; j< enemyBullets.size();j++)
    		{
    			
    		if(bullets.get(i).bulletPoly.intersects(enemyBullets.get(j).bulletPoly))
    				{
    			score+=15;
    			bullets.remove(bullets.get(i));
    			enemyBullets.remove(enemyBullets.get(j));
    			break;
    				}
    		}
    		
    	}
    	
    	if(powerupShield)
    	{
    		for(int j=0; j< enemyBullets.size();j++)
    		{
    		if(enemyBullets.get(j).bulletPoly.intersects(planeShield))
    				{
    			enemyBullets.remove(enemyBullets.get(j));
    				}
    		    
    		 }
    		
    		for(int j=0; j< enemies.size();j++)
    		{
    		if(enemies.get(j).enemyPoly.intersects(planeShield))
    				{
    					enemies.remove(enemies.get(j));
    				}
    		    
    		 }
    	}
    	if(!powerupShield){
    	//Plane and Enemy Collide
		for(int j=0; j< enemies.size();j++)
		{
		if(enemies.get(j).enemyPoly.intersects(planePoly))
				{
			if(health > 0)
			{
				health-=20;
			
			
			/*if(healthBar.bars > 0)
			{
				healthBar.bars -= 1; 
			}*/
			}
			
			if(healthBar.bars < 0)
			{
				health = 0;
			}
			
			enemies.remove(enemies.get(j));
				}
		    
		 }
    	}
		//Plane/Enemy Collide end
    	
    	
    	//Plane and powerup Collide
    			for(int j=0; j< powerups.size();j++)
    			{
    			if(powerups.get(j).powerupPoly.intersects(planePoly))
    					{
    						powerupShield = true;
    						
    				powerups.remove(powerups.get(j));
    					}
    			    
    			 }
    	    	if(powerupShield)
    	    	{
    	    		timer += delta;
    	    		  	    		
    	    		System.out.println(delta);
    	    		if(timer > 5000)
    	    		{
    	    			powerupShield = false;
    	    			timer = 0;
    	    		}
    	    	}
    			//Plane/powerup Collide end
		
		
    	//Plane and Bullet Collide
    	if(!powerupShield){
		for(int j=0; j< enemyBullets.size();j++)
		{
		if(enemyBullets.get(j).bulletPoly.intersects(planePoly))
				{
			if(health > 0)
			{
				health -= enemyBullets.get(j).damage;
			
			/*if(healthBar.bars > 0)
			{
				healthBar.bars -= 1; 
			}*/
			}
			healthBar.barOutlinePoly = new Image("assets/bar"+healthBar.bars+".png");
			if(healthBar.bars < 0)
			{
				health = 0;
			}
			enemyBullets.remove(enemyBullets.get(j));
				}
		    
		 } }
		//Plane/Bullet Collide end
    		
    	//collision end	----------------------
    	if(lastSpawn == 0)
    	{
        	java.util.Random random = new java.util.Random();
    		spawnInterval = 500+random.nextInt(2000);
    	}
    	lastSpawn += delta;

    	//HealthBars
    	if(health <= 0)
    	{
    		health = 0;
    		healthBar.bars = 0; 
    	}
    	else if(health <= 20)
    	{
    		healthBar.bars = 1; 
    	}
    	else if(health <= 40)
    	{
    		healthBar.bars = 2; 
    	}
    	else if(health <= 60)
    	{
    		healthBar.bars = 3; 
    	}
    	else if(health <= 80)
    	{
    		healthBar.bars = 4;
    	}
    	else
    	{
    		healthBar.bars = 5; 
    	}
    	
    	healthBar.barOutlinePoly = new Image("assets/bar"+healthBar.bars+".png");
    	

    	
    	//HealthBars End
    	if(lastSpawn > spawnInterval){
        enemies.add(new Enemy());
        powerups.add(new Powerup());
        lastSpawn = 0;
        }
    	
    	float move = .001f * delta;
        x-= move;
        planePoly.setX(x);
    	
    	lastFire += delta;
        planeShield.setX(x-31);
        planeShield.setY(y-23);
    	
    	
    	
        if(input.isKeyDown(Input.KEY_LCONTROL) && playing)
        {
        	hip = 0.2f * delta;
        	
        } 
        else
        {
        	hip = 0.4f * delta;

        	
        }
        if(input.isKeyDown(Input.KEY_A) && playing)
        {
         // float hip = 0.4f * delta;
          x-= hip;
          planePoly.setX(x);

        }
 
        if(input.isKeyDown(Input.KEY_D) && playing)
        {
          //float hip = 0.4f * delta;  
          x+= hip;
          planePoly.setX(x);
        }
 
        if(input.isKeyDown(Input.KEY_W) && playing)
        {
            //float hip = 0.4f * delta;
 
            
            y-= hip ;
            planePoly.setY(y);
        }
        if(input.isKeyDown(Input.KEY_S) && playing)
        {
           // float hip = 0.4f * delta;
 
 
            y+= hip;
            planePoly.setY(y);
        }
 
        if(input.isKeyDown(Input.KEY_2))
        {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
        }
        if(input.isKeyPressed(Input.KEY_ESCAPE))
        {
        	music.stop();
        	sbg.enterState(SimpleGame.MAINMENUSTATE, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
        if(input.isKeyPressed(Input.KEY_ENTER))
        {
        	
        	currentState = STATES.PAUSE_GAME_STATE;
        	
        }

       // if(Keyboard.isKeyDown(Input.KEY_LCONTROL))
       // {
        //	float hip = 0.4f * delta;
       // }
        if((input.isKeyDown(Input.KEY_SPACE) ||  input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) && playing)
        {
            //float hip = 0.4f * delta;
        	
        	if(bulletPassing < 200 && cooldown == false){
        	if(lastFire > 80){
        	bullets.add(new Bullet(x+112,y+26,2,20));
            laser.play(1f,.3f);
            lastFire = 0; 
            bulletRecharge++;
            bulletPassing += delta *.5f;
        	}
        	}
        	else
			{
        		cooldown = true;
        		if(bulletPassing > 0)
        		{
        			bulletPassing -= delta * .05f;
        			
        		}
				
        		if(bulletPassing == 0)
    			{
    				cooldown = false;
    			}
				
			}
        	
        	}
        else
        {
        	if(bulletPassing > 0)
        	{
        	bulletPassing -= delta * .08f;
        	}
        	else
        	{
        		bulletPassing = 0;
        		cooldown = false;
        	}
        }
        if ( input.isMousePressed(Input.MOUSE_RIGHT_BUTTON) ){
    		//sbg.enterState(SimpleGame.MAINMENUSTATE);
    		if(powerupShield)
    		{
    			powerupShield = false;
    		}
    		else {
    			powerupShield = true;
    		}
    	}
        //--------------------------

    	healthBar.update();
    	chargeBar.update();
        //---------------------------
    	if(bulletPassing >= 160)
    	{
    		chargeBar.bars = 5;
    	}
    	else if(bulletPassing > 120)
    	{
    		chargeBar.bars = 4;
    	}
    	else if(bulletPassing > 80)
    	{
    		chargeBar.bars = 3;
    	}
    	else if(bulletPassing > 40)
    	{
    		chargeBar.bars = 2;
    	}
    	else if(bulletPassing > 0)
    	{
    		chargeBar.bars = 1;
    	}
    	else
    	{
    		chargeBar.bars = 0;
    	}	
    	
    	if(cooldown == false)
		{
			chargeBar.barOutlinePoly = new Image("assets/bar"+chargeBar.bars+".png");
		}
		else
		{
			chargeBar.barOutlinePoly = new Image("assets/redbar"+chargeBar.bars+".png");
			music.stop();
		}
    	
    	
	
		
        //---------------------------
    	for(int i = 0; i < enemies.size(); i++)
    	{
    		
    		enemies.get(i).timePassed += delta *.5f;
    		enemies.get(i).updateInterval = delta;

    		//System.out.println(playing);
    		
    	//	enemies.get(i).enemyPoly.draw(enemies.get(i).enemyPoly);
    	//  g.draw(enemies.get(i).enemyPoly);   // --- DRAW HITBOX
    		
        	//	powerups.get(i).powerupPoly.draw(powerups.get(i).powerupPoly);
    	}
    	
    	deltaTime = delta;


        //---------END-------------
    	
    	if(!playing)
    	{
    		endTimer += delta * .5f;
    	}
    	
    	//---------------------------
    }
 

    
    
    public void render(GameContainer gc, StateBasedGame sbg ,Graphics g) 
			throws SlickException 
    {
    	
    	/*//Render powerupPoly
    	for(int i = 0; i < powerups.size(); i++)
    	{
        		g.draw(powerups.get(i).powerupPoly);
    	}
    	//Render end*/

    	for(int i = 0; i < backgrounds.size(); i++)
    	{
    		//backgrounds.get(i).posX-=1;
    		backgrounds.get(i).background.draw(backgrounds.get(i).posX ,backgrounds.get(i).posY);
    		backgrounds.get(i).update();
    		if(backgrounds.get(i).posX < -3200)
    		{
    			backgrounds.remove(backgrounds.get(i));
    		}
    		if(backgrounds.get(i).posX == -2400 && backgrounds.get(i).tile == 1)
    		{
    	    	backgrounds.add(new Background(800,0,1));
    	    	backgrounds.add(new Background(1200,0,2));
    	    	backgrounds.add(new Background(1600,0,3));
    	    	backgrounds.add(new Background(2000,0,4));    	
    	    	backgrounds.add(new Background(2400,0,5));
    	    	backgrounds.add(new Background(2800,0,6));
    	    	backgrounds.add(new Background(3200,0,7));
    	    	backgrounds.add(new Background(3600,0,8));
    		}

    	}
    	for(int i = 0; i < bullets.size(); i++)
    	{
    		bullets.get(i).bulletGraphic.draw(bullets.get(i).posX ,bullets.get(i).posY);
    		bullets.get(i).update(10);
    		if(bullets.get(i).posX > 800)
    		{
    			bullets.remove(bullets.get(i));
    		}
    	}
    	for(int i = 0; i < enemyBullets.size(); i++)
    	{
    		enemyBullets.get(i).bulletGraphic.draw(enemyBullets.get(i).posX ,enemyBullets.get(i).posY);
    		enemyBullets.get(i).update(-10);
    		if(enemyBullets.get(i).posX < -10)
    		{
    			enemyBullets.remove(enemyBullets.get(i));
    		}
    	}
    	
    	for(int i = 0; i < powerups.size(); i++)
    	{
    		powerups.get(i).powerupGraphic.draw(powerups.get(i).posX ,powerups.get(i).posY);
    		powerups.get(i).update();
    		if(powerups.get(i).posX < -20)
    		{
    			powerups.remove(powerups.get(i));
    			if(health > 0)
    			{
    				//enemiesPassed += 1;
    			}
    			
    		}
    	}
    	
    	for(int i = 0; i < enemies.size(); i++)
    	{
    		enemies.get(i).enemyGraphic.draw(enemies.get(i).posX ,enemies.get(i).posY);
    		enemies.get(i).update();
    		if(enemies.get(i).posX < -20)
    		{
    			enemies.remove(enemies.get(i));
    			if(health > 0)
    			{
    				enemiesPassed += 1;
    			}
    			
    		}
    		
    	//	enemies.get(i).enemyPoly.draw(enemies.get(i).enemyPoly);
    	//  g.draw(enemies.get(i).enemyPoly);   // --- DRAW HITBOX
    	//	g.draw(planePoly);                    // HITBOX DRAW

    	}
    	
    	
    	


    	plane.draw(x,y,scale);
		 if(powerupShield){
			 //g.draw(planeShield); //Shield hitbox
			 shield.draw(x-30,y-23);
		 }
    	//Text now
    	g.drawString("Score: " + score,200,10);
    	//g.drawString("Charge: " + bulletPassing,400,40);
    	if(cooldown)
    	{
    		g.drawString("OVERLOAD!",400,40);
    	}
    	else
    	{
    		g.drawString("Heat",415,40);
    	}
    	g.drawString("Health: " + health,600,40);
    	g.drawString("Pass: " + enemiesPassed,600,80);
    	if(health<=0)
    	{
    		currentState = STATES.GAME_OVER_STATE;
    		g.drawString("GAME OVER",350,180);
    		if(playing)
    		{
    			
    		smoke.play();
    		music.stop();
    		playing = false;
    		//currentState = STATES.GAME_OVER_STATE;
    		//currentState = STATES.START_GAME_STATE;
    		}
    		
    		if(endTimer > 500)
        	{
        		g.drawString("Press Escape", 340, 200);
        		
        	}
    		//Game pause until enter somehow
    	}
    	healthBar.barOutlinePoly.draw(600,7);
    	chargeBar.barOutlinePoly.draw(400,7);
    	
    	//g.draw(chargeBar.barOutlinePoly);
    }
    
   

  
    

}
