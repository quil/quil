(ns docs.cheat-sheet-gen
  (:require [processing.core]))

(def cheat-sheet-start
  "
\\documentclass[footexclude,twocolumn,DIV40,fontsize=8.7pt]{scrreprt}

% Author: Sam Aaron
% Comments, errors, suggestions: sam.aaron(at)gmail.com

% Based on Steve Tayon's excellent Clojure Cheat Sheet

% License
% Eclipse Public License v1.0
% http://opensource.org/licenses/eclipse-1.0.php

% Packages
\\usepackage[utf8]{inputenc}
\\usepackage[T1]{fontenc}
\\usepackage{textcomp}
\\usepackage[english]{babel}
\\usepackage{tabularx}
\\usepackage{lmodern}
\\renewcommand*\\familydefault{\\sfdefault}


\\usepackage[table]{xcolor}

% Set column space
\\setlength{\\columnsep}{0.25em}

% Define colours
\\definecolorset{hsb}{}{}{red,0,.4,0.95;orange,.1,.4,0.95;green,.25,.4,0.95;grey,0.0,0.0,0.1;yellow,.15,.4,0.95}

\\definecolorset{hsb}{}{}{blue,.55,.4,0.95;purple,.7,.4,0.95;pink,.8,.4,0.95;blue2,.58,.4,0.95}

\\definecolorset{hsb}{}{}{magenta,.9,.4,0.95;green2,.29,.4,0.95}

% Redefine sections
\\makeatletter
\\renewcommand{\\section}{\\@startsection{section}{1}{0mm}
  {-1.7ex}{0.7ex}{\\normalfont\\large\\bfseries}}
\\renewcommand{\\subsection}{\\@startsection{subsection}{2}{0mm}
  {-1.7ex}{0.5ex}{\\normalfont\\normalsize\\bfseries}}
\\makeatother

% No section numbers
\\setcounter{secnumdepth}{0}

% No indentation
\\setlength{\\parindent}{0em}

% No header and footer
\\pagestyle{empty}


% A few shortcuts
\\newcommand{\\cmd}[1] {\\frenchspacing\\texttt{\\textbf{{#1}}}}
\\newcommand{\\cmdline}[1] {
  \\begin{tabularx}{\\hsize}{X}
    \\texttt{\\textbf{{#1}}}
  \\end{tabularx}
}

\\newcommand{\\colouredbox}[2] {
  \\colorbox{#1!40}{
    \\begin{minipage}{0.95\\linewidth}
      {
        \\rowcolors[]{1}{#1!20}{#1!10}
        #2
      }
    \\end{minipage}
  }
}


%%%%%%%% BEGIN DOC %%%%%%%%%%%%

\\begin{document}

 {\\Large{\\textbf{Processing-core Cheat Sheet 1.0.0}}}

")

(def cheat-sheet-end
  "
 \\begin{flushright}
  \\footnotesize
  \\rule{0.7\\linewidth}{0.25pt}
  \\verb!$Revision: 1.0.0.0, $Date: 4th March, 2012!\\\\
  \\verb!Sam Aaron (sam.aaron gmail com)!
 \\end{flushright}

\\end{document}


")

(def cheat-sheet-applet
  "
\\colouredbox{blue1}{
  \\section{Applet}
  \\begin{tabularx}{\\hsize}{lX}
 Creation & \\cmd{applet defapplet} \\\\
 Control & \\cmd{applet-stop applet-start applet-exit applet-close} \\\\
 Config Keywords & \\cmd{:title :size} \\\\
 Draw Callback Keywords & \\cmd{:setup :draw} \\\\
 Mouse Callback Keywords & \\cmd{:mouse-pressed :mouse-released :mouse-moved :mouse-dragged :mouse-entered :mouse-exited :mouse-clicked} \\\\
 Keyboard Callback Keywords & \\cmd{:key-pressed :key-released :key-typed } \\\\
 Window Callback Keywords & \\cmd{:focus-gained :focus-lost} \\\\
  \\end{tabularx}
}
")

(def cheat-sheet-docs
  "
\\colouredbox{grey}{
  \\section{Reflection}
  \\begin{tabularx}{\\hsize}{lX}
API Exploration & \\cmd{doc-cats doc-fns doc-meths} \\\\
Version & \\cmd{processing-version} \\\\
  \\end{tabularx}
}

")

(defn cheat-sheet-dynamic
  []
  (let [colours (cycle ["blue2" "yellow" "purple" "green" "orange" "pink" "blue" "yellow" "grey" "red" "green" "blue" "pink" "yellow"])
        info    (#'processing.core/sorted-category-map)
        clean   (fn [s] (.replaceAll s "&" "\\\\&"))]
    (dorun
     (map (fn [[cat-idx cat-info] colour]
            (println (str "\\colouredbox{" colour "}{"))
            (println (str "  \\section{"(clean (:name cat-info)) "}"))
            (println "  \\begin{tabularx}{\\hsize}{lX}")
            (when (> (count (:fns cat-info)) 0)
              (print (str "   & \\cmd{" ))
              (dorun (map (fn [f] (print f " ")) (:fns cat-info)))
              (println "} \\\\"))
            (dorun
             (map (fn [[subcat-idx {:keys [name fns]}]]
                    (when (> (count fns) 0)
                      (print (str "   " (clean name) " & \\cmd{" ))
                      (dorun (map (fn [f] (print f " ")) fns))
                      (println "} \\\\")))
                  (:subcategories cat-info)))
            (println "  \\end{tabularx}")
            (println "}"))
          info colours))))



(defn mk-cheat-sheet
  []
  (println cheat-sheet-start)
  (cheat-sheet-dynamic)
  (println cheat-sheet-applet)
  (println cheat-sheet-docs)
  (println  cheat-sheet-end)
)
