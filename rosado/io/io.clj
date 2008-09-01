;; simple io abstraction layer for clojure
;; Roland Sadowski [szabla gmail com]

(in-ns 'io)
(clojure/refer 'clojure)

(import '(java.io File FileReader FileWriter BufferedReader BufferedWriter
				  FileInputStream FileOutputStream FileOutputStream
				  BufferedInputStream BufferedOutputStream))

(defn reader 
  "Returns a reader or input stream (buffered).
  optional keywords: :unbuff [:ub] - for unbuffered input. 
  :bytes - for an InputStream instead of a Reader."
  [#^String file-name & modes]
  (cond
   (nil? modes) (BufferedReader. (FileReader. file-name))
   (some #{:bytes} modes) (if (some #{:unbuff :ub} modes)
							(FileInputStream. file-name)
							(BufferedInputStream. (FileInputStream. file-name)))
   (some #{:unbuff :ub} modes) (FileReader. file-name)))

(defn read-lines 
  "Returns a list of lines from seq."
  [#^java.io.BufferedReader rdr]
  (loop [lines [] line (.readLine rdr)]
	(if (nil? line)
	  lines
	  (recur (conj lines line) (.readLine rdr)))))

(defn writer
  "Returns a writer or output stream (buffered).
  optional keywords: :unbuff [:ub] - for unbuffered output. 
  :bytes - for an OutputStream instead of a Writer."
  [#^String file-name & modes]
  (cond
   (nil? modes) (BufferedWriter. (FileWriter. file-name))
   (some #{:bytes} modes) (if (some #{:unbuff :ub} modes)
							(FileOutputStream. file-name)
							(BufferedOutputStream. (FileOutputStream. file-name)))
   (some #{:unbuff :ub} modes) (FileWriter. file-name)))

(defn write-lines 
  "Writes a seq of lines to a writer. Appends a newline to ecah line."
  [#^java.io.BufferedWriter a-writer lines]
  (loop [lns lines]
	(when lns
	  (.write a-writer (first lns))
	  (.newLine a-writer)
	  (recur (rest lns)))))
