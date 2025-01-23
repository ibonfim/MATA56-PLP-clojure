(ns datashow)

;; Estrutura inicial com a lista de datashows
(def datashows
  [{:id 1 :status :disponivel}
   {:id 2 :status :disponivel}
   {:id 3 :status :alocado}
   {:id 4 :status :disponivel}])

;; Função para consultar o status de um datashow
(defn consultar [datashows id]
  (loop [ds datashows]
    (cond
      (empty? ds) nil
      (= (:id (first ds)) id) (:status (first ds))
      :else (recur (rest ds)))))

;; Função para alocar um datashow
(defn alocar [datashows id]
  (loop [ds datashows]
    (cond
      (empty? ds) (str "Datashow " id " não encontrado.")
      (= (:id (first ds)) id)
      (if (= (:status (first ds)) :disponivel)
        (cons (assoc (first ds) :status :alocado) (rest ds))  ;; Atualiza o status do datashow
        (str "Datashow " id " já está alocado."))
      :else (recur (rest ds)))))

;; Função para desalocar um datashow
(defn desalocar [datashows id]
  (loop [ds datashows]
    (cond
      (empty? ds) (str "Datashow " id " não encontrado.")
      (= (:id (first ds)) id)
      (if (= (:status (first ds)) :alocado)
        (cons (assoc (first ds) :status :disponivel) (rest ds))  ;; Atualiza o status do ds
        (str "Datashow " id " não estava alocado."))
      :else (recur (rest ds)))))

;; Função auxiliar para exibir o estado atual da lista de datashows
(defn exibir-datashows [datashows]
  (doseq [ds datashows]
    (println "ID:" (:id ds) "Status:" (:status ds))))

;; Exemplos de uso
(defn -main []
  ;; Exibir os datashows iniciais
  (println "Estado inicial dos datashows:")
  (exibir-datashows datashows)
  (println)

  ;; Consultar status
  (println "Consultando status do datashow 1:" (consultar datashows 1))
  (println "Consultando status do datashow 3:" (consultar datashows 3))
  (println)

  ;; Alocar um datashow
  (println "Alocando datashow 1:")
  (def datashows (alocar datashows 1)) ;; Reatribui a lista após a alocação
  (exibir-datashows datashows)
  (println)

  ;; Tentar alocar um datashow já alocado
  (println "Tentando alocar datashow 3:")
  (def datashows (alocar datashows 3)) ;; Reatribui após tentar alocar
  (exibir-datashows datashows)
  (println)

  ;; Desalocar um datashow
  (println "Desalocando datashow 3:")
  (def datashows (desalocar datashows 3)) ;; Reatribui após desalocar
  (exibir-datashows datashows))

;; Executar o código principal
(-main)
