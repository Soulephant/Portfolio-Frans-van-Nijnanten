#Een geleend switch statement, omdat het om de een of andere reden niet standaard
#in python zit. De class heet witch omdat ik de module altijd importeer als witch,
#oftewel S.witch.

class witch(object):
    def __init__(self, value):
        self.value = value
        self.fall = False

    def __iter__(self):
        """Return the match method once, then stop (origineel)"""
        yield self.match
        raise StopIteration
    
    def match(self, *args):
        """Indicate whether or not to enter a case suite (origineel)"""
        if self.fall or not args:
            return True
        elif self.value in args: # changed for v1.5, see below (origineel)
            self.fall = True
            return True
        else:
            return False