
from dis import dis
import random


class GrapheNonOriente:

    def __init__(self,*liaison):
        
        self.noeud = []
        self.liaison = [e for e in liaison]

        self.liste_adjacence = {}
        self.mat = None

        self.update()

    def ajouterLiason(self,noeud1,noeud2,distance=1):
        print('AJOUT DE LA LIAISON : ',noeud1,noeud2,distance)
        #dans les init classique:
        if noeud1 not in self.noeud:
            self.noeud.append(noeud1)

            self.mat.append([0 for e in range(len(self.noeud)-1)])
            for e in self.mat:
                e.append(0)

            self.liste_adjacence[noeud1] = {}

        if noeud2 not in self.noeud:
            self.noeud.append(noeud2)

            self.mat.append([0 for e in range(len(self.noeud)-1)])
            for e in self.mat:
                e.append(0)

            self.liste_adjacence[noeud2] = {} 

        print("update des noeud : ", self.noeud)

        if (noeud1,noeud2,distance) not in self.liaison and (noeud2,noeud1,distance) not in self.liaison :
            self.liaison.append((noeud1,noeud2,distance))
        print("update des liaison : ", self.liaison)

        #dans la liste adjacence:
        if noeud2 not in self.liste_adjacence[noeud1]:
            self.liste_adjacence[noeud1][noeud2] = distance
        if noeud1 not in self.liste_adjacence[noeud2]:
            self.liste_adjacence[noeud2][noeud1] = distance
        print("update de la liste adjacence : ",self.liste_adjacence)

        #dans la mat:
        self.mat[ self.noeud.index(noeud1) ][ self.noeud.index(noeud2) ] = distance
        self.mat[ self.noeud.index(noeud2) ][ self.noeud.index(noeud1) ] = distance
        print("update de la mat : ")
        for i in self.mat:
            print(i)

        print('')

    def ajouterPlusieurLiaison(self,*arg):
        for e in arg:
            self.ajouterLiason(*e)

    def update(self):

        print('')
        #maj de la liste de noeud du graphe
        
        for e in self.liaison:
            if e[0] not in self.noeud:
                self.noeud += [ e[0] ]
            if e[1] not in self.noeud:
                self.noeud += [ e[1] ]
        print("update des noeud : ", self.noeud)

        #maj de la mat
        self.mat = [[ 0 for j in range(len(self.noeud))] for i in range(len(self.noeud))]
        for i in self.liaison:
            if len(i) < 3:
                self.mat[ self.noeud.index(i[0]) ][ self.noeud.index(i[1]) ] = 1
                self.mat[ self.noeud.index(i[1]) ][ self.noeud.index(i[0]) ] = 1
            else:
                self.mat[ self.noeud.index(i[0]) ][ self.noeud.index(i[1]) ] = i[2]
                self.mat[ self.noeud.index(i[1]) ][ self.noeud.index(i[0]) ] = i[2]
        print("update de la mat : ")
        for i in self.mat:
            print(i)

        #maj de la liste adjacence
        for e in self.noeud:
            self.liste_adjacence[e] = {}
        for e in self.liaison:
            if len(e) < 3:
                self.liste_adjacence[e[0]][e[1]] = 1
                self.liste_adjacence[e[1]][e[0]] = 1
            else:    
                self.liste_adjacence[e[0]][e[1]] = e[2]
                self.liste_adjacence[e[1]][e[0]] = e[2]
        
        print("update de la liste adhacence : ",self.liste_adjacence)

        print('')

    def parcourLargeur(self,noeud_depart):

        file = [noeud_depart]
        parcour_largeur = [noeud_depart]

        while len(file)>0:
            x = file.pop(0)
            for e in self.liste_adjacence[x].keys():
                if e not in parcour_largeur:
                    file.append(e)
                    parcour_largeur.append(e)
        
        print(parcour_largeur)

    def parcourProfondeur(self,noeud,pile=[],parcour=[]):
    
        pile.append(noeud)
        parcour.append(noeud)
        while len(pile)>0:
            x = pile.pop()
            for e in self.liste_adjacence[x].keys():
                if e not in parcour:
                    self.parcourProfondeur(e,pile,parcour)
        
        return parcour

    def contienAuMoinUnCycle(self):

        noeud_depart = self.noeud[random.randint(0,len(self.noeud)-1)]

        pile = [noeud_depart]
        noir = []

        while len(pile)>0:
            x = pile.pop()
            for e in self.liste_adjacence[x].keys():
                if e not in noir:
                    pile.append(e)

            if x in noir:
                return True
            else:
                noir.append(x)

        return False

    def trouveChaineEntre(self,debut,fin,chaine=[]):

        chaine.append(debut)

        if debut == fin:
            return chaine
        
        for e in self.liste_adjacence[debut].keys():
            if e not in chaine:
                nchemin = self.trouveChaineEntre(e,fin,chaine)
                if len(nchemin)>0:
                    return nchemin
            
        return []

    def PlusCourtChemin(self,noeudD,noeudA):

        #basé sur l'algo de dijkstra

        def trouve_min(sousGraphe,distance):
            min = -1
            sommet = -1
            for e in sousGraphe:
                if min == -1 and distance[self.noeud.index(e)] != -1:
                    min = distance[self.noeud.index(e)]
                    sommet = e
                elif distance[self.noeud.index(e)] < min and distance[self.noeud.index(e)] != -1 :
                    min = distance[self.noeud.index(e)]
                    sommet = e
            return sommet

        def maj_distance(s1,s2):
            if (distance[self.noeud.index(s2)] > distance[self.noeud.index(s1)] + self.liste_adjacence[s1][s2]) or distance[self.noeud.index(s2)] == -1 :
                distance[self.noeud.index(s2)] = distance[self.noeud.index(s1)] + self.liste_adjacence[s1][s2]
                self.predecesseur[s2] = s1

        # INNIT:
        distance = [-1 for e in range(len(self.noeud))]
        distance[self.noeud.index(noeudD)] = 0
        sousGraphe = []
        chemin = []
        for e in self.noeud:
            sousGraphe.append(e)
        self.predecesseur = {}
        for e in self.noeud:
            self.predecesseur[e] = ""

        #DEBUT
        while(len(sousGraphe)>0):
            s1 = trouve_min(sousGraphe,distance)
            sousGraphe.pop(sousGraphe.index(s1))
            for s2 in self.liste_adjacence[s1].keys():
                maj_distance(s1,s2)

        #pour retrouver le chemin grave au dico predecesseur
        while (noeudA != noeudD):
            chemin.insert(0,noeudA)
            noeudA = self.predecesseur[noeudA]
        chemin.insert(0,noeudA)

        return chemin