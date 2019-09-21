����   2� $androidx/test/uiautomator/UiSelector  java/lang/Object  UiSelector.java SELECTOR_NIL I     SELECTOR_TEXT    SELECTOR_START_TEXT    SELECTOR_CONTAINS_TEXT    SELECTOR_CLASS    SELECTOR_DESCRIPTION    SELECTOR_START_DESCRIPTION    SELECTOR_CONTAINS_DESCRIPTION    SELECTOR_INDEX    SELECTOR_INSTANCE   	 SELECTOR_ENABLED   
 SELECTOR_FOCUSED    SELECTOR_FOCUSABLE    SELECTOR_SCROLLABLE    SELECTOR_CLICKABLE    SELECTOR_CHECKED    SELECTOR_SELECTED    SELECTOR_ID    SELECTOR_PACKAGE_NAME    SELECTOR_CHILD    SELECTOR_CONTAINER    SELECTOR_PATTERN    SELECTOR_PARENT    SELECTOR_COUNT    SELECTOR_LONG_CLICKABLE    SELECTOR_TEXT_REGEX    SELECTOR_CLASS_REGEX    SELECTOR_DESCRIPTION_REGEX    SELECTOR_PACKAGE_NAME_REGEX    SELECTOR_RESOURCE_ID    SELECTOR_CHECKABLE    SELECTOR_RESOURCE_ID_REGEX    mSelectorAttributes Landroid/util/SparseArray; .Landroid/util/SparseArray<Ljava/lang/Object;>; <init> ()V J K
  L android/util/SparseArray N
 O L G H	  Q this &Landroidx/test/uiautomator/UiSelector; )(Landroidx/test/uiautomator/UiSelector;)V cloneSelector (()Landroidx/test/uiautomator/UiSelector; V W
  X selector
  L clone ()Landroid/util/SparseArray; \ ]
 O ^ hasChildSelector ()Z ` a
  b getChildSelector d W
  e J U
  g put (ILjava/lang/Object;)V i j
 O k hasParentSelector m a
  n getParentSelector p W
  q hasPatternSelector s a
  t getPatternSelector v W
  w ret patternBuilder N(Landroidx/test/uiautomator/UiSelector;)Landroidx/test/uiautomator/UiSelector; patternSelector | {
  } t(Landroidx/test/uiautomator/UiSelector;Landroidx/test/uiautomator/UiSelector;)Landroidx/test/uiautomator/UiSelector; containerSelector � {
  � 	container pattern text :(Ljava/lang/String;)Landroidx/test/uiautomator/UiSelector; "java/lang/IllegalArgumentException � text cannot be null � (Ljava/lang/String;)V J �
 � � buildSelector ;(ILjava/lang/Object;)Landroidx/test/uiautomator/UiSelector; � �
  � Ljava/lang/String; textMatches regex cannot be null � java/util/regex/Pattern � compile -(Ljava/lang/String;)Ljava/util/regex/Pattern; � �
 � � regex textStartsWith textContains 	className className cannot be null � classNameMatches 9(Ljava/lang/Class;)Landroidx/test/uiautomator/UiSelector; T<T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;)Landroidx/test/uiautomator/UiSelector; type cannot be null � java/lang/Class � getName ()Ljava/lang/String; � �
 � � type Ljava/lang/Class<TT;>; Ljava/lang/Class; description desc cannot be null � desc descriptionMatches descriptionStartsWith descriptionContains 
resourceId id cannot be null � id resourceIdMatches index )(I)Landroidx/test/uiautomator/UiSelector; java/lang/Integer � valueOf (I)Ljava/lang/Integer; � �
 � � instance enabled )(Z)Landroidx/test/uiautomator/UiSelector; java/lang/Boolean � (Z)Ljava/lang/Boolean; � �
 � � val Z focused 	focusable 
scrollable selected checked 	clickable 	checkable longClickable childSelector selector cannot be null � 
fromParent packageName name cannot be null � name packageNameMatches getLastSubSelector � W
  � 
selectorId selectorValue Ljava/lang/Object; get '(ILjava/lang/Object;)Ljava/lang/Object; � �
 O � getContainerSelector getInstance ()I getInt (I)I � �
  � 	getString (I)Ljava/lang/String; java/lang/String � 	criterion 
getBoolean (I)Z booleanValue � a
 � � intValue � �
 � � 
getPattern (I)Ljava/util/regex/Pattern; 
isMatchFor 6(Landroid/view/accessibility/AccessibilityNodeInfo;I)Z size �
 O keyAt �
 O java/lang/CharSequence 0android/view/accessibility/AccessibilityNodeInfo	 	isChecked a

 � �
  getClassName ()Ljava/lang/CharSequence;

 toString � � �
  contentEquals (Ljava/lang/CharSequence;)Z
 � � �
  matcher 3(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher; 
 �! java/util/regex/Matcher# matches% a
$& isClickable( a

) isCheckable+ a

, isLongClickable. a

/ getContentDescription1

2 toLowerCase4 �
 �5 contains7
 �8 
startsWith (Ljava/lang/String;)Z:;
 �< getText>

? 	isEnabledA a

B isFocusableD a

E 	isFocusedG a

H getPackageNameJ

K isScrollableM a

N 
isSelectedP a

Q getViewIdResourceNameS �

T matchOrUpdateInstanceV a
 W s Ljava/lang/CharSequence; x node 2Landroid/view/accessibility/AccessibilityNodeInfo; 
indexOfKey^ �
 O_ (I)Ljava/lang/Object; �a
 Ob currentSelectorCounter currentSelectorInstance isLeaf hasContainerSelector child parent dumpToString (Z)Ljava/lang/String;jk
 l java/lang/StringBuildern
o L getSimpleNameq �
 �r append -(Ljava/lang/String;)Ljava/lang/StringBuilder;tu
ov [x
o , { TEXT=} valueAta
 O� -(Ljava/lang/Object;)Ljava/lang/StringBuilder;t�
o� TEXT_REGEX=� START_TEXT=� CONTAINS_TEXT=� CLASS=� CLASS_REGEX=� DESCRIPTION=� DESCRIPT