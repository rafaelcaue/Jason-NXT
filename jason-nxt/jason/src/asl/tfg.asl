/* Crenças e regras iniciais */

goal(search).

/* Objetivos iniciais */
!init.

/* Planos */

+!init <- block(true); !!move.
				
// Finaliza quando encontra a linha preta
+light(_, X)[source(percept)] 
					: goal(search) & X < 400
					<- stop([a,b]); exit.

// Detecta o obstáculo
+obstacle(_, X)[source(percept)]
			: 	goal(search) & X < 20
			<- 	-+goal(avoid);
				!!avoid.

// Desvia do obstáculo
+!avoid	<-	stop([a,b]); speed([a,b],[300,300]);
 				rotate([a,b],[-200,200]); rotate([a,b],[400,400]); 
 				rotate([a,b],[200,-200]); rotate([a,b],[800,800]); 
 				rotate([a,b],[200,-200]); rotate([a,b],[400,400]); 
 				rotate([a,b],[-200,200]); !!move.
 				
// move-se lentamente
+!move <- forward([a,b],[60,60]).