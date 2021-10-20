import requests


def get_code_definition(id):

  url = "https://www.wikidata.org/w/api.php"
  
  params = {
        "action" : "wbgetentities",
        "languages" : "en",
        "format" : "json",
        "ids" : id
        }
  data = requests.get(url, params=params).json()

  return data['entities'][id]['labels']['en']['value']


def search_wikidata_for_human(name):
  url = "https://www.wikidata.org/w/api.php"

  params = {
        "action" : "wbsearchentities",
        "language" : "en",
        "format" : "json",
        "search" : name
        }

  data = requests.get(url, params=params).json()
  id = data['search'][0]['id']

  params = {
        "action" : "wbgetentities",
        "languages" : "en",
        "format" : "json",
        "ids" : id 
        }
    
  data = requests.get(url, params=params).json()
  
  return get_all_values_from_all_claims(data, id)


def get_all_values_from_all_claims(data, id):
  claims = data['entities'][id]['claims']
  all_claims = {"Name" : data['entities'][id]['labels']['en']['value'], "Description" : data['entities'][id]['descriptions']['en']['value']}


  for key in claims.keys():

    current_claim = []

    claim = claims[key]

    for value in claim:
      sub_claim = value['mainsnak']['datavalue']['value']

      if type(sub_claim) == dict and 'id' in sub_claim.keys():
        current_claim.append(get_code_definition(sub_claim['id']))

      elif type(sub_claim) == dict and 'text' in sub_claim.keys() :
        current_claim.append(sub_claim['text'])

      elif type(sub_claim) == dict and 'amount' in sub_claim.keys() :
        current_claim.append(sub_claim['amount'])
        
      elif type(sub_claim) == dict and 'time' in sub_claim.keys() :
        current_claim.append(sub_claim['time'])

      else:
        current_claim.append(sub_claim)  

    all_claims.update({get_code_definition(key) : current_claim})
  return all_claims  
