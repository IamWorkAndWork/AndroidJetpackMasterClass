package app.practice.dogs.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import app.practice.dogs.R
import app.practice.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    var viewModel: ListViewModel? = null
    var dogsListAdapter: DogsListAdapter? = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel?.refresh()

        val lm = LinearLayoutManager(activity)
        dogsList?.layoutManager = lm
        dogsList?.adapter = dogsListAdapter

        observeViewModel()

        swipeRefresh?.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError?.visibility = View.GONE
            loadingView?.visibility = View.VISIBLE
//            viewModel?.refresh()
            viewModel?.refreshByPassCache()
        }

        setHasOptionsMenu(true)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionSetting -> {
                view?.let {
                    Navigation.findNavController(it).navigate(ListFragmentDirections.actionSetting())
                }
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun observeViewModel() {

        viewModel?.dogs?.observe(activity!!, Observer {
            it?.let { dogs ->
                dogsList.visibility = View.VISIBLE
                dogsListAdapter?.updateDogList(dogs.toCollection(arrayListOf()))

                swipeRefresh?.isRefreshing = false
            }
        })

        viewModel?.dogsLoadError?.observe(activity!!, Observer {
            it?.let {
                listError?.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel?.loading?.observe(activity!!, Observer {
            it?.let {
                loadingView?.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    dogsList.visibility = View.GONE
                }
            }
        })

    }


}
