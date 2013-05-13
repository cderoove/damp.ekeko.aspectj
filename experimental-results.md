# Experimental results #

We implemented Aspect Assumptions as identified by Zschaler and Rashid in their paper ["Aspect assumptions: a retrospective study of AspectJ developers' assumptions about aspect usage"](http://dx.doi.org/10.1145/1960275.1960288). We restrict ourselves to the assumptions in section 3.1.1 of their paper as an implementation of all assumptions is outside of the scope of our work. Also, the code analyzed was HealthWatcher and MobileMedia only, Glassbox causes a compiler crash.

## 1: Inclusion assumptions ###

### Implementation ###

Formally in the paper: weaving context of A1 is that A2 is deployed. 

> An aspect A1 relies on another aspect A2 to either modify the behaviour A1 introduces or to introduce base behaviour that A1 can then modify.

> A special case is when an aspect A1 has been introduced to resolve a feature interaction between two other aspects A2 and A3 (or more).

> Beyond the general case, we have identified a specific variation, where one aspect defines a marker interface that is then used by another aspect in a declare parents clause.

#### Case 1 ####

A1 relies on another aspect A2 to modify the behaviour A1 introduces. The semantics of this are not formalized. It seems to refer to A2 having join point shadows in the advice of A1, which modifies the behaviour of A1. 

```
(defn 
  modifies|aspect1-aspect2
  [?modifier ?modified] ;;A2 and A1
  (l/fresh [?advice ?shadow]
         (aspect-advice ?modifier ?advice)
         (advice-shadow ?advice ?shadow)
         (l/conde [;;shadow on 
                   (shadow-enclosing ?shadow ?modified)] 
                  [;;shadow within 
                   (shadow-ancestor|type ?shadow ?modified)]) 
         (aspect ?modified)
         (l/!= ?modifier ?modified)))
```

#### Case 2 ####

A1 relies on another aspect A2 to introduce base behaviour that A1 can then modify. The semantics of this are not formalized. This seems to refer to A2 advising something, and A1 having shadows in the advice. ** This is actually idem to case 1 because it is the dual. **

```
(defn aspect-on-aspect [?modifier ?modified] ;;A1 and A2
	(fresh [?advice ?shadow]
		(aspect-advice ?modifier ?advice)
		(advice-shadow ?advice ?shadow)
		(shadow-enclosingtypedeclaration ?shadow ?modified)
		(aspect ?modified)))
```

#### Special case 1 ####

Formally: weaving context of A1 is that A2 and A3 are deployed.

The semantics are not clear from the text. It seems to state the following relationship using the inclusion assumption in case 1 and 2.

** This mock code is untested -- no plans to implement due to being too ambiguous in the paper **

```
(defn interaction-triplet-resolver-inter1-inter2 [?asp0 ?asp1 ?asp2]
	(all
		(modifies-aspect ?asp0 ?asp1)
		(modifies-aspect ?asp0 ?asp1)))
```

#### Special case 2 ####

There is an empty interface. (The paper said that it is defined by an aspect, but this may also be a simple top-level interface declarations) Optionally aspects define (nested) interfaces that extend it. Aspects do a declare parents of the empty interface or of the interfaces they define.

```
(defn
  markerinterface
  [?interface]
  (l/fresh [?member]
    (interface ?interface)
    (fails (type-member ?interface ?member))))
;;
(defn
  iface-self|or|sub
  [?interface ?subinterface]
  (l/all
    (interface ?interface)
    (interface ?subinterface)
   (l/conde [(type-declaredinterface+ ?subinterface ?interface)]
            [(l/== ?subinterface ?interface)])))
;;
(defn
  aspect-declareparents|markerinterface
  [?aspect ?interface]
  (l/fresh [?superinterface ?declare]
    (markerinterface ?superinterface)
    (iface-self|or|sub ?superinterface ?interface)
    (declare|parents-parent|type ?declare ?interface)
    (aspect-declare ?aspect ?declare)))
```

### Experiments ###

#### Case 1 & 2 ####

** HealthWatcher **

Match from the paper:

> Aspect HWTimeStamp had to be adjusted to provide special behaviour for the case when UpdateStateObserver (implementing the observer protocol) is deployed.

There are no straightforward matches with the paper. We do see some indications. The rule we envisioned is too simplistic to capture the kinds of dependencies talked about in the paper.

```
(
[#<ReferenceType healthwatcher.aspects.concurrency.HWTimestamp> #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver>]
[#<ReferenceType healthwatcher.aspects.concurrency.HWTimestamp> #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver>]
[#<ReferenceType healthwatcher.aspects.concurrency.HWTimestamp> #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver>]
[#<ReferenceType healthwatcher.aspects.concurrency.HWTimestamp> #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver>]
[#<ReferenceType lib.patterns.CommandProtocol> #<ReferenceType healthwatcher.aspects.patterns.ServletCommanding>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.persistence.HWTransactionManagement> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>] 
[#<ReferenceType healthwatcher.aspects.distribution.RMIServerDistribution> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.distribution.RMIServerDistribution> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.distribution.RMIServerDistribution> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
[#<ReferenceType healthwatcher.aspects.distribution.RMIServerDistribution> #<ReferenceType healthwatcher.aspects.distribution.HWServerDistribution>]
)
```

** MobileMedia **

There are no straightforward matches with the paper, but we found some interesting facts (below the results).

> deploying PhotoAndMusicAspect implies features for both photo and music management have been selected. As the features themselves have been implemented in separate aspects (PhotoSelector and MusicSelector in this example), there is an inclusion requirement that these implementation aspects also be deployed.

What actually happens here is that P&M sets some options of the main controller that then later P or M read at some point. So there is a control flow from P&M to P or M later on. The rule we envisioned is too simplistic to capture this kinds of dependencies.


```
([#<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect> #<ReferenceType lancs.mobilemedia.optional.copy.CopyMultiMediaAspect>]
[#<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect> #<ReferenceType lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect> #<ReferenceType lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.copy.CopyMultiMediaAspect> #<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect>]
[#<ReferenceType lancs.mobilemedia.optional.copy.CopyMultiMediaAspect> #<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect>]
[#<ReferenceType lancs.mobilemedia.optional.copy.CopyMultiMediaAspect> #<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect>]
[#<ReferenceType lancs.mobilemedia.optional.copy.CopyMultiMediaAspect> #<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.alternative.music.MusicAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect> #<ReferenceType lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect>]
[#<ReferenceType lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect> #<ReferenceType lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect>]
[#<ReferenceType lancs.mobilemedia.alternative.photo.optional.CopyAndPhoto> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.alternative.photo.optional.CopyAndPhoto> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
)
```

Regarding the 4 last matches (2 are dupes): 

* CaptureVideoAspect->CapturePhotoAspect the pointcut matches too much (constructor calls in both CaptureVideoAspect & CapturePhotoAspect) and the advice filters out the unwanted (CapturePhotoAspect). This may be classified as a bad implementation, the test should actually be in an if pointcut. 
* CopyAndPhoto->PhotoAspect Adds copy command functionality to code in the photoaspect. So this is exactly what the assumption is about.


#### Special case 1 ####

SKIP

#### Special case 2 ####

The paper only talks about Glassbox. No matches in paper data for the HW or MM projects.

** HealthWatcher **

Direct

```
([#<ReferenceType healthwatcher.aspects.concurrency.HWLocalSynchronization> #<ReferenceType healthwatcher.aspects.concurrency.HWLocalSynchronization$SynchronizedClasses>]
[#<ReferenceType healthwatcher.aspects.patterns.ServletCommanding> #<ReferenceType lib.patterns.CommandReceiver>]
[#<ReferenceType healthwatcher.aspects.persistence.HWDataCollection> #<ReferenceType healthwatcher.aspects.persistence.HWDataCollection$SystemRecord>]
[#<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver> #<ReferenceType lib.patterns.ObserverProtocol$Observer>]
[#<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver> #<ReferenceType lib.patterns.ObserverProtocol$Subject>])
```

1. First and third are self dependencies. We could update the rule to filter this out but leave it as it reveals interesting info nonetheless.
2. Second case is dependency on an empty interface that, according to its comments, was created for aspects to mark: "This interface is used by extending aspects ...". 
3. Last 2 cases are clear matches, these nested interfaces, according to their comments, were created for aspects to mark: "This interface is used by extending aspects ..."

Note that items 2 and 3 are not mentioned in the results files nor in the paper!! 

Sub interface of marker: 0 items found

** MobileMedia **

Direct: 0 items found

Sub interface of marker: 0 items found

## 2: Mutual exclusion assumptions ##

### Implementation ###

Formally in the paper: weaving context of A1 is that A2 is NOT deployed.

This is a semantic decision that cannot be unambiguously defined in a code rule.
Possible Heuristics:

1. 2 aspects define a pointcut with the same name
2. 2 aspects are subs of one (that defines pointcuts), both use the same pointcuts.
3. 2 aspects match on exactly the same join point shadows

#### Case 1 ####

```
(defn
  same|pointcut|name-aspect1-aspect2
  [?name ?aspect1 ?aspect2]
  (l/fresh [?pc1 ?pc2]
     (aspect-pointcutdefinition ?aspect1 ?pc1)
     (aspect-pointcutdefinition ?aspect2 ?pc2)
     (l/!= ?aspect1 ?aspect2)
     (pointcutdefinition-name ?pc1 ?name)
     (pointcutdefinition-name ?pc2 ?name)))
```

#### Case 2 ####

This only checks whether there are 2 subs of a super and that they use the same pointcuts.
If both use the same pointcuts, we don't need to restrict that these come from the superaspect because both could use public pointcuts of some third aspect. On the other hand, if they refine abstract pointcuts, the pointcuts they actually use are different ones.

```
(defn 
  aspect-usedpointcutdef
  [?aspect ?pointcutdef]
  (l/fresh [?advice]
           (aspect-advice ?aspect ?advice)
           (advice-pointcutdefinition ?advice ?pointcutdef)))
;;
(defn aspect-allusedpointcutdefs
  [?aspect ?usedpointcutdefs]
  (l/all
    (aspect ?aspect)
    (findall ?usedpcdef (aspect-usedpointcutdef ?aspect ?usedpcdef) ?usedpointcutdefs)))
;;
(defn
  samepointcuts|reuse|from|super|sub1-sub2-usedpc
  [?aspect1 ?aspect2 ?usedpc1 ]
  (l/fresh [?superaspect ?usedpc2]
           (aspect-declaredsuperaspect+ ?aspect1 ?superaspect)
           (aspect-declaredsuperaspect+ ?aspect2 ?superaspect)
           (l/!= ?aspect1 ?aspect2)
           (aspect-allusedpointcutdefs ?aspect1 ?usedpc1)
           (aspect-allusedpointcutdefs ?aspect2 ?usedpc2)
           (l/!= ?usedpc1 [])
           (l/!= ?usedpc2 [])
           (same-elements ?usedpc1 ?usedpc2)
           ))
```

#### Case 3 ####

```
(defn
  sameshadows|aspect1-aspect2
  [?aspect1 ?aspect2]
  (l/fresh [?shadows1 ?shadows2]
           (aspect ?aspect1)
           (aspect ?aspect2)
           (l/!= ?aspect1 ?aspect2)
           (findall ?shadow1 (aspect-shadow ?aspect1 ?shadow1) ?shadows1)
           (findall ?shadow2 (aspect-shadow ?aspect2 ?shadow2) ?shadows2)
           (l/!= ?shadows1 [])
           (l/!= ?shadows2 [])
           (same-elements ?shadows1 ?shadows2)))
```


### Experiments ###

> HWLocalSynchronization and HWManagedSynchronization providing alternative implementations for the synchronisation of access to data classes of HealthWatcher.

Inspecting the code shows that ```HWManagedSynchronization``` only does it on inserts, while ```HWLocalSynchronization``` also does it on updates and deletes et.al. (In the paper the mutex was revealed through a developer interview). If they would have the same functionality they would match on the same operations though.  So, were that true, it could be matched in case 1 or case 3

> RMIClientDistribution […] contains pointcuts using withincode(HWClientDistribution+) (referring to the super-aspect of RMIClientDistribution).
> These pointcuts would also—erroneously—match joinpoints in other sub-aspects of HWClientDistribution [but there are none right now]

If there are other sub-aspects they would probably match case 2 as there is only 1 pointcut used in ```RMIClientDistribution``` and it is defined in the parent.


#### Case 1 ####


** HealthWatcher **

```
(["subjectChange" #<ReferenceType lib.patterns.ObserverProtocol> #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver>] ["commandTrigger" #<ReferenceType healthwatcher.aspects.patterns.ServletCommanding> #<ReferenceType lib.patterns.CommandProtocol>] ["commandTrigger" #<ReferenceType lib.patterns.CommandProtocol> #<ReferenceType healthwatcher.aspects.patterns.ServletCommanding>] ["subjectChange" #<ReferenceType healthwatcher.aspects.patterns.UpdateStateObserver> #<ReferenceType lib.patterns.ObserverProtocol>])
```

Both are cases of implementation of abstract pc. 

** MobileMedia **

These are different command actions implemented as aspects (command pattern variation): ```handleCommandAction``` , ```initMenu```, ```constructor``` pointcut name. So we update the rule on the fly to exclude these : (note results are duplicated due to symmetry)

```
=> (ekeko [?name ?as1 ?as2]
                   (l/all (same|pointcut|name-aspect1-aspect2 ?name ?as1 ?as2)
                          (l/!= ?name "handleCommandAction")
                          (l/!= ?name "constructor")
                          (l/!= ?name "initMenu")))
(["initForm" #<ReferenceType lancs.mobilemedia.alternative.video.optional.CopyAndVideo> #<ReferenceType lancs.mobilemedia.alternative.music.optional.CopyAndMusic>]
["appendMedias" #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>]
["createMediaData" #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>]
["getMediaController" #<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect> #<ReferenceType lancs.mobilemedia.optional.copy.CopyAspect>]
["resetMediaData" #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH> #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.DataModelAspectEH>]
["showImage" #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>]
["initForm" #<ReferenceType lancs.mobilemedia.alternative.music.optional.CopyAndMusic> #<ReferenceType lancs.mobilemedia.alternative.video.optional.CopyAndVideo>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.AbstractAlternativeFeature> #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.AbstractAlternativeFeature> #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect>]
["getMediaController" #<ReferenceType lancs.mobilemedia.optional.copy.CopyAspect> #<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo> #<ReferenceType lancs.mobilemedia.alternative.AbstractAlternativeFeature>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo> #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect>]
["goToPreviousScreen" #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo> #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect> #<ReferenceType lancs.mobilemedia.alternative.AbstractAlternativeFeature>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect> #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo>]
["goToPreviousScreen" #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect> #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo>]
["showImage" #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH>]
["appendMedias" #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect>]
["createMediaData" #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect>]
["getBytesFromImageInfo" #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect> #<ReferenceType lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect>]
["getBytesFromImageInfo" #<ReferenceType lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>]
["resetMediaData" #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.DataModelAspectEH> #<ReferenceType lancs.mobilemedia.aspects.exceptionblocks.ControllerAspectEH>])
```

In this we see 6(*2) times duplicate pointcut code, in :

```
["createMediaData" #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>] 
["getMediaController" #<ReferenceType lancs.mobilemedia.optional.sms.SMSAspect> #<ReferenceType lancs.mobilemedia.optional.copy.CopyAspect>]
["goToPreviousScreen" #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo> #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect>]
["initForm" #<ReferenceType lancs.mobilemedia.alternative.video.optional.CopyAndVideo> #<ReferenceType lancs.mobilemedia.alternative.music.optional.CopyAndMusic>]
["appendMedias" #<ReferenceType lancs.mobilemedia.optional.favourites.FavouritesAspect> #<ReferenceType lancs.mobilemedia.optional.sorting.SortingAspect>]
["startApp" #<ReferenceType lancs.mobilemedia.alternative.AbstractAlternativeFeature> #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo>]
```

#### Case 2 ####

** HealthWatcher **

0 matches found.

** MobileMedia **

```
([#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic>
  [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic>
  [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.video.VideoAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.video.VideoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.favourites.FavouritesAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photo.PhotoAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.music.MusicAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photo.PhotoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.music.MusicAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sorting.SortingAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect> #<ReferenceType lancs.mobilemedia.alternative.video.VideoAspect>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.capturevideo.CaptureVideoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.video.VideoAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.video.VideoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.favourites.FavouritesAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photo.PhotoAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.music.MusicAspect.constructor(lancs.mobilemedia.core.ui.controller.AbstractController)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photo.PhotoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.music.MusicAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.capturephoto.CapturePhotoAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sorting.SortingAspect.initMenu(lancs.mobilemedia.core.ui.screens.MediaListScreen)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]]
;
[#<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo> #<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic>
 [#<ResolvedPointcutDefinition pointcut lancs.mobilemedia.optional.sms.SMSAspect.startApplication(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.AbstractAlternativeFeature.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.PhotoAndMusicAndVideo.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)> #<ResolvedPointcutDefinition pointcut lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect.startApp(lancs.mobilemedia.core.ui.MainUIMidlet)>]])
```

* PhotoNotVideoNotMusic, MusicNotPhotoNotVideo, VideoNotPhotoNotMusic: these have specific initialization code, hence not mutex
* PhotoAspect, VideoAspect: adding of commands to view photos/play videos, hence not mutex


#### Case 3 ####

** HealthWatcher **

```
([#<ReferenceType healthwatcher.aspects.exceptionHandling.HWTransactionExceptionHandler> #<ReferenceType healthwatcher.aspects.exceptionHandling.HWDistributionExceptionHandler>]
[#<ReferenceType healthwatcher.aspects.exceptionHandling.HWDistributionExceptionHandler> #<ReferenceType healthwatcher.aspects.exceptionHandling.HWTransactionExceptionHandler>])
```

These are complimentary: one catching transaction exceptions, one catching communication and RMI exceptions

** MobileMedia **

There is some duplication here because of the aforementioned use of the command pattern

```
([#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic>]
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic>]
[#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo>]
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic> #<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo>]
[#<ReferenceType lancs.mobilemedia.alternative.video.VideoAspect> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect>]
[#<ReferenceType lancs.mobilemedia.alternative.MusicSelector> #<ReferenceType lancs.mobilemedia.alternative.VideoSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.MusicSelector> #<ReferenceType lancs.mobilemedia.alternative.PhotoSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.VideoSelector> #<ReferenceType lancs.mobilemedia.alternative.MusicSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.VideoSelector> #<ReferenceType lancs.mobilemedia.alternative.PhotoSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.photo.PhotoAspect> #<ReferenceType lancs.mobilemedia.alternative.video.VideoAspect>]
[#<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo> #<ReferenceType lancs.mobilemedia.alternative.photo.PhotoNotVideoNotMusic>]
[#<ReferenceType lancs.mobilemedia.alternative.music.MusicNotPhotoNotVideo> #<ReferenceType lancs.mobilemedia.alternative.video.VideoNotPhotoNotMusic>]
[#<ReferenceType lancs.mobilemedia.alternative.OneAlternativeFeature> #<ReferenceType lancs.mobilemedia.alternative.TwoAlternativeFeatures>]
[#<ReferenceType lancs.mobilemedia.alternative.PhotoSelector> #<ReferenceType lancs.mobilemedia.alternative.MusicSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.PhotoSelector> #<ReferenceType lancs.mobilemedia.alternative.VideoSelector>]
[#<ReferenceType lancs.mobilemedia.alternative.TwoAlternativeFeatures> #<ReferenceType lancs.mobilemedia.alternative.OneAlternativeFeature>]
[#<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo> #<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect>]
[#<ReferenceType lancs.mobilemedia.alternative.photoMusic.PhotoAndMusicAspect> #<ReferenceType lancs.mobilemedia.alternative.PhotoAndMusicAndVideo>])
```
First 4 add initialization code on startup.

Found 1 mutex case: ```[#<ReferenceType lancs.mobilemedia.alternative.OneAlternativeFeature> #<ReferenceType lancs.mobilemedia.alternative.TwoAlternativeFeatures>]```
their name already is an indication. Both add an exit command to a menu. Not in the paper but yes in the data.

## 3: Mandatory aspect assumptions ##

### Implementation ###

> An aspect-oriented system may contain one or more aspects that are required by all other aspects in the system and are mandatory for the system to work. [...] Mandatory aspect assumptions cannot be expressed by inclusion assumptions from all other aspects, as this could still be satisfied by not deploying any aspects at all, including the mandatory ones. 

This is a semantic decision that cannot be unambiguously defined in a code rule. Unless the assumption is specified as metadata to the code. Implementing verification this way requires some reasoning over these assumption specifications. Hence out of the scope of this work.

## 4: Assumptions on ITD provision ##

### Implementation ###

> An aspect may make use of features that need to be introduced through an ITD from another aspect.

> Such assumptions are easily checked by ajc as the ITDs will need to be available at compile time.

We do not provide analysis for this as the compiler already provides it

## 5: Assumptions on ITD use ##

### Implementation ###

Formally in the paper: the weaving context of A is that somebody invokes the ITD's of A

Special case is that somebody calls the ITDs of A before any advice of A is executed.

#### General case ####

```
(defn 
  intertype|method|unused
  [?itmethod]
  (l/fresh [?sootmethod ?caller]
         (intertype|method ?itmethod)
         (fails 
           (l/all
             (ajsoot/intertype|method-soot|method ?itmethod ?sootmethod)
             (jsoot/soot-method-called-by-method ?sootmethod ?caller)))))
```

### Experiments ###

In the paper is only the special case, which we are skipping for now.

#### General Case ####

** HealthWatcher **

See healthwatcher.view.servlets.ServletWebServer for the entry point for soot.

Check again with healthwatcher.business.HealthWatcherFacade

HW is a server + a set of servlets to be executed by a Java web server. We do not include analysis of this web server, which means that we do not have all possible execution paths through the servlets of HW. We only analyse the execution paths of the server part of the app.

Because of the above, we reject after manual verification as these are reached from the servlet entry point:

```
([#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, long healthwatcher.model.complaint.Complaint.getTimestamp()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void healthwatcher.model.complaint.Complaint.setTimestamp(long)))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void healthwatcher.model.complaint.Complaint.incTimestamp()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, healthwatcher.data.factories.AbstractRepositoryFactory healthwatcher.data.factories.AbstractRepositoryFactory.getRepositoryFactory()))>]
```

The following ITD is not found because the ITD itself is the main. Coen should update Soot control panel (issue [11](https://github.com/cderoove/damp.ekeko.aspectj/issues/11) )

```
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void healthwatcher.business.HealthWatcherFacade.main(java.lang.String[])))>]
)
```

Found:
```
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, boolean lib.patterns.Command.isExecutable()))>]
```
Is a default implementation for the method declared in an abstract class. Yet this method is never referenced. Code comment:
```
This interface method is optional (default: all commands are excutable); a default 
implementation is provided by the abstract CommandProtocol aspect.
```
This is not mentioned in the results files nor in the paper!

** MobileMedia **

```lancs.mobilemedia.core.ui.MainUIMidlet``` contains the entry point of the app: ```startApp()```. This however does not take into account user actions coming from the UI, ```lancs.mobilemedia.core.ui.controller.AbstractController``` method ```void commandAction(Command c, Displayable d)``` from the ```javax.microedition.lcdui.CommandListener``` interface. 

False positives, unknown why these turned up. :-(
```
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, boolean lancs.mobilemedia.alternative.photo.PhotoViewScreen.isFromSMS()))>] 
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, boolean lancs.mobilemedia.core.ui.controller.MediaController.playVideoMedia(java.lang.String)))>]
```

Found:
```
([#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.controller.BaseController lancs.mobilemedia.alternative.SelectMediaController.getImageController()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.datamodel.AlbumData lancs.mobilemedia.alternative.SelectMediaController.getImageAlbumData()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, byte[] lancs.mobilemedia.alternative.photo.PhotoViewScreen.getImage()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, javax.microedition.lcdui.Image lancs.mobilemedia.core.ui.screens.AddMediaToAlbum.getImage()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void lancs.mobilemedia.core.ui.screens.AddMediaToAlbum.setImage(javax.microedition.lcdui.Image)))>] 
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.optional.capture.CaptureVideoScreen lancs.mobilemedia.optional.copySMS.PhotoViewController.getCpVideoScreen()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void lancs.mobilemedia.optional.capture.CaptureVideoScreen.takePicture()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, void lancs.mobilemedia.core.ui.datamodel.AlbumData.addVideoData(java.lang.String, java.lang.String, byte[])))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.controller.BaseController lancs.mobilemedia.alternative.SelectMediaController.getVideoController()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.datamodel.AlbumData lancs.mobilemedia.alternative.SelectMediaController.getVideoAlbumData()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.controller.BaseController lancs.mobilemedia.alternative.SelectMediaController.getMusicController()))>]
[#<BcelTypeMunger (BcelTypeMunger ResolvedTypeMunger(Method, lancs.mobilemedia.core.ui.datamodel.AlbumData lancs.mobilemedia.alternative.SelectMediaController.getMusicAlbumData()))>])
```

None of the above cases are present in the paper nor in the results files for the paper!


## 6: Assumptions on the semantics of (abstract) pointcuts ##

### Implementation ###

> This category groups assumptions an aspect A1 makes about how its super-aspect A2 advises pointcuts it (A1) introduces.

> When a sub-aspect decides to refine [an abstract] pointcut, it implicitly makes an assumption about the advice provided for this pointcut by the super-aspect

#### General case ####

```
(defn
  refine|used|pointcut-sub-super
  [?pointcutdef ?subaspect ?aspect]
  (l/fresh [?newpointcutdef ?advice]
    (aspect-pointcutdefinition ?aspect ?pointcutdef)
    (pointcutdefinition|abstract ?pointcutdef)
    (pointcutdefinition-concretizedby ?pointcutdef ?newpointcutdef)
    (aspect-pointcutdefinition ?subaspect ?newpointcutdef)
    (aspect-advice ?aspect ?advice)
    (advice-pointcutdefinition ?advice ?newpointcutdef)))
```

### Experiments ###

The paper only talks about Glassbox. No matches in paper data for the HW or MM projects.

** HealthWatcher **

0 matches

** MobileMedia **

0 matches

## 7: Assumptions on concretisation of pointcuts ##

### Implementation ###

> An aspect A1 defining a pointcut p, but wishing to maintain the behaviour introduced by its super-aspect makes an assumption that this pointcut is not already defined by its super-aspect(s) A

Note: this needs to take into account redefinition of pointcuts, which is only blocked by AJ if the pointcut is final.

#### General case ####

```
(defn 
  abstractpointcutdefinition-concretized-reconcretized
  [?abpointcut ?concpointcut1 ?concpointcut2]
  (l/all
    (pointcutdefinition-concretizedby ?abpointcut ?concpointcut1)
    (pointcutdefinition-concretizedby ?concpointcut1 ?concpointcut2)))
```


### Experiments ###

This case is only present in Glassbox.

** HealthWatcher **

0 matches - corresponds with paper

** MobileMedia **

0 matches - corresponds with paper 

## 8: Assumptions on sub-aspect structure (pointcut contracts) ##

### Implementation ###

>When an aspect declares an abstract pointcut it makes certain assumptions about the joinpoints that will be matched by any concretisation of the pointcut in a sub-aspect. [...] these assumptions work more as a contract that an abstract aspect would like to impose on its specialisations. 

Implementing verification of this requires some contract specification (see above), which is out of the scope of this work.


## 9: Precedence assumptions ##

### Implementation ###

> This may mean expecting precedence to be explicitly declared in a particular way. Alternatively, it may mean that the standard AspectJ precedence (in particular between sub- and super-aspect) should not be modified by an explicit declare precedence clause.

#### Case 1 ###

This requires some way to annotate what this explicit precedence should be. This can and should be done explicitly in the aspect itself, as is stated in the paper.

#### Case 2 ###

```
(defn
  overriden|implicit|precedence
  [?first ?second]
  (l/all
    (aspect|dominates-aspect ?second ?first)
    (aspect|dominates|implicitly-aspect+ ?first ?second)))
```

### Experiments ###

Paper: hw.a.exceptionHandling.ExceptionHandlingPrecedence.aj; 44
This is case 1.

#### Case 2 ###

This case is present in Glassbox.

** HealthWatcher **

0 matches - corresponds with paper. 

** MobileMedia **

0 matches - corresponds with paper. 
