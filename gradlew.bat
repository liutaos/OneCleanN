����   2 M &androidx/test/uiautomator/Configurator  java/lang/Object  Configurator.java mWaitForIdleTimeout J mWaitForSelector mWaitForActionAcknowledgment mScrollEventWaitTimeout mKeyInjectionDelay 	mToolType I DEFAULT_UIAUTOMATION_FLAGS     mUiAutomationFlags sConfigurator (Landroidx/test/uiautomator/Configurator; <init> ()V  
        '  	    	        � 	 	         � 
 	  #  	  %  	  '  	  ) this getInstance *()Landroidx/test/uiautomator/Configurator;  	  .
   setWaitForIdleTimeout +(J)Landroidx/test/uiautomator/Configurator; timeout getWaitForIdleTimeout ()J setWaitForSelectorTimeout getWaitForSelectorTimeout setScrollAcknowledgmentTimeout getScrollAcknowledgmentTimeout setActionAcknowledgmentTimeout getActionAcknowledgmentTimeout setKeyInjectionDelay delay getKeyInjectionDelay setToolType +(I)Landroidx/test/uiautomator/Configurator; toolType getToolType ()I setUiAutomationFlags flags getUiAutomationFlags ConstantValue Code LineNumberTable LocalVariableTable StackMapTable 
SourceFile 1     	            	     
                  G          
          H   z     0*� * � * � * �  * !� $*	� &*� (*� *�    I   & 	   6  !  "  #  '   * % - * 1 / 8 J       0 +    	 , -  H   =      � /� � Y� 0� /� /�    K     I       A  B  D  1 2  H   ?     *� *�    I   
    W  X J        +       3    4 5  H   /     *� �    I       j J        +     6 2  H   ?     *� *�    I   
    {  | J        +       3    7 5  H   /     *� �    I       � J        +     8 2  H   ?     *� $*�    I   
    �  � J        +       3    9 5  H   /     *� $�    I       � J        +     : 2  H   ?     *�  *�    I   
    �  � J        +       3    ; 5  H   /     *�  �    I       � J        +     < 2  H   ?     *� &*�    I   
    �  � J        +       =    > 5  H   /     *� &�    I       � J        +     ? @  H   ?     *� (*�    I   
    �  � J        +       A    B C  H   /     *� (�    I       � J    