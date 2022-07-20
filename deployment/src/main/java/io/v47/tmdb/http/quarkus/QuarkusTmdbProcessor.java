package io.v47.tmdb.http.quarkus;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;
import io.quarkus.jackson.spi.ClassPathJacksonModuleBuildItem;
import io.v47.tmdb.http.QuarkusHttpClientConfiguration;
import io.v47.tmdb.jackson.TmdbApiModule;
import io.v47.tmdb.jackson.TmdbCoreModule;
import io.v47.tmdb.model.TmdbType;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

class QuarkusTmdbProcessor {
    private static final DotName TMDB_TYPE = DotName.createSimple(TmdbType.class.getName());

    private static final String FEATURE = "tmdb-api-client";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    public void enableSslNativeSupport(BuildProducer<ExtensionSslNativeSupportBuildItem> sslNativeSupport) {
        sslNativeSupport.produce(new ExtensionSslNativeSupportBuildItem(FEATURE));
    }

    @BuildStep
    public void registerJacksonModules(BuildProducer<ClassPathJacksonModuleBuildItem> jacksonModules) {
        jacksonModules.produce(new ClassPathJacksonModuleBuildItem(TmdbCoreModule.class.getName()));
        jacksonModules.produce(new ClassPathJacksonModuleBuildItem(TmdbApiModule.class.getName()));
    }

    @BuildStep
    public void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
        indexDependency.produce(new IndexDependencyBuildItem("com.neovisionaries", "nv-i18n"));
        indexDependency.produce(new IndexDependencyBuildItem("io.v47.tmdb-api-client", "api"));
        indexDependency.produce(new IndexDependencyBuildItem("io.v47.tmdb-api-client", "core"));
        indexDependency.produce(new IndexDependencyBuildItem("org.jetbrains.kotlin", "kotlin-stdlib"));
    }

    @BuildStep
    public void registerTmdbModelClasses(CombinedIndexBuildItem combinedIndex,
                                         BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchyClass,
                                         BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        Type tmdbType = Type.create(TMDB_TYPE, Type.Kind.CLASS);
        reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(tmdbType)
                                                                                   .serialization(true)
                                                                                   .ignoreTypePredicate(new IgnoreKotlinTypes())
                                                                                   .build());

        combinedIndex.getIndex().getAllKnownSubclasses(TMDB_TYPE).forEach(classInfo -> {
            Type jandexType = Type.create(classInfo.name(), Type.Kind.CLASS);
            reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(jandexType)
                                                                                       .serialization(true)
                                                                                       .build());
        });

        additionalBeans.produce(new AdditionalBeanBuildItem(QuarkusHttpClientConfiguration.class));
    }

    @BuildStep(onlyIf = IsTckActive.class)
    public void addTestDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
        indexDependency.produce(new IndexDependencyBuildItem("io.v47.tmdb-api-client", "http-client-tck"));
    }

    @BuildStep(onlyIf = IsTckActive.class)
    public void registerTckClasses(BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchyClass,
                                   BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        List<String> tckClasses = Arrays.asList("io.v47.tmdb.http.tck.tests.AbstractTckTest",
                                                "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest",
                                                "io.v47.tmdb.http.tck.tests.ValidSimpleResponseTest");

        List<String> tckSerializableClasses = Arrays.asList("io.v47.tmdb.http.tck.TckResult",
                                                            "io.v47.tmdb.http.tck.TckResult$Success",
                                                            "io.v47.tmdb.http.tck.TckResult$Failure",
                                                            "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest$CompanyAlternativeNames",
                                                            "io.v47.tmdb.http.tck.tests.ValidComplexResponseTest$CompanyAlternativeNames$AlternativeName",
                                                            "io.v47.tmdb.http.tck.tests.ValidSimpleResponseTest$Company");

        tckClasses.forEach(name -> {
            reflectiveClass.produce(ReflectiveClassBuildItem.builder(name).constructors(true).fields(true).build());
        });

        tckSerializableClasses.forEach(name -> {
            Type jandexType = Type.create(DotName.createSimple(name), Type.Kind.CLASS);

            reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(jandexType)
                                                                                       .serialization(true)
                                                                                       .build());
        });
    }

    static class IgnoreKotlinTypes implements Predicate<DotName> {
        private static final List<DotName> ignoredNames = Arrays.asList(DotName.createSimple("kotlin.Lazy"),
                                                                        DotName.createSimple(
                                                                                "kotlin.jvm.functions.Function0"));

        @Override
        public boolean test(DotName dotName) {
            return !ignoredNames.contains(dotName);
        }
    }

    static class IsTckActive implements BooleanSupplier {
        @Override
        public boolean getAsBoolean() {
            ClassNotFoundException classNotFoundException = null;

            try {
                Class.forName("io.v47.tmdb.http.tck.HttpClientTck");
            } catch (ClassNotFoundException x) {
                System.out.println("io.v47.tmdb.http.tck.HttpClientTck not in classpath");
                classNotFoundException = x;
            }

            return classNotFoundException == null;
        }
    }
}
