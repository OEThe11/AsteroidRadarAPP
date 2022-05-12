package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.AsteroidViewAdapter.OnClickListener

class MainFragment : Fragment() {

//    private lateinit var  manager: RecyclerView.LayoutManager

  private lateinit var viewModel: MainViewModel

  private val adapter = AsteroidViewAdapter(AsteroidViewAdapter.OnClickListener {
        viewModel.navigateToDetails(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)


        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

//        val mutableList: MutableList<Asteroid> = ArrayList()
//        mutableList.add(Asteroid(1, "fgnuugrhrg", "bihagtyjerwailgubivb", 4.0, 8.0,3.0, 9.0, false))
//        mutableList.add(Asteroid(2, "fguk.nuugrhrg", "bidjswjyhagrwailgubivb", 3.0, 90.0,355.0, 9.0, true))
//        mutableList.add(Asteroid(3, "fgnssuugrhrg", "bshjtihagrwailgubivb", 4.0, 33.0,33.0, 9.0, false))
//        mutableList.add(Asteroid(4, "fgnuw4suugrhrg", "bjsryjihagrwailgubivb", 6.0, 8.0,11.0, 9.0, true))
//        mutableList.add(Asteroid(5, "fgnuugrudkdkhrg", "bihjjkkuagrwailgubivb", 4.0, 5.0,77.0, 9.0, false))

//        manager = LinearLayoutManager(this.context)



        //connecting the adapter
        binding.asteroidRecycler.adapter = adapter

        viewModel.info.observe(viewLifecycleOwner, Observer {
            viewModel.progress(it.isNullOrEmpty())
            binding.asteroidRecycler.smoothScrollToPosition(0)
            adapter.submitList(it)
        })

       viewModel.navigation.observe(viewLifecycleOwner, Observer { asteroid ->
           asteroid?.let {
               findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
               viewModel.navigationDone()
           }
       })


//        binding.asteroidRecycler.adapter = adapter


        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_show_week -> {
                viewModel.filter(Filter.WEEK)
                true
            }
            R.id.menu_show_today -> {
                viewModel.filter(Filter.TODAY)
                true
            }
            R.id.menu_show_saved -> {
                viewModel.filter(Filter.SAVED)
                true
            }
            else -> false
        }
    }
}



