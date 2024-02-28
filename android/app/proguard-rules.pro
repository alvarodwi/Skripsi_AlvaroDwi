# Get rid of package names, makes file smaller
-repackageclasses

# --> Data classes
-keep class me.varoa.nongki.domain.model.** { *; }
-keep class me.varoa.nongki.data.local.entity.** { *; }

# --> ViewBinding
# ViewBindingDelegate uses Reflection.
-keepclassmembers class ** implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);
}

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
