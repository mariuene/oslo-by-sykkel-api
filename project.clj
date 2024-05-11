(defproject oslo-by-sykkel-api "0.0.1-SNAPSHOT"
    :description "FIXME: write description"
    :url "http://example.com/FIXME"
    :min-lein-version "2.0.0"
    :dependencies [[org.clojure/clojure "1.10.0"]
                   [compojure "1.6.1"]
                   [http-kit "2.3.0"]
                   [cheshire "5.13.0"]
                   [clj-http "3.13.0"]
                   [ring/ring-defaults "0.3.2"]
                   [org.clojure/data.json "0.2.6"]]
    :main ^:skip-aot oslo-by-sykkel-api.core
    :target-path "target/%s"
    :profiles {:uberjar {:aot :all
                         :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
